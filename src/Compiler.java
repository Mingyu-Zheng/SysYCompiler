import error.Error;
import frontend.vn.CompUnit;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import utils.Reader;
import utils.Writer;

public class Compiler {

    Reader reader;
    Writer writer;

    static CompUnit unit;
    static SymbolTable symbolTable;

    public static void main(String args[]){
        Compiler compiler = new Compiler("testfile.txt","error.txt");
        Token token = null;
        while((token = Token.getToken(compiler.reader)) != null){
            Token.addToken(token);
        }
        unit = new CompUnit();
        unit.RCompUnit();
        symbolTable = new SymbolTable(1);
        unit.RAnalysis(symbolTable);
        compiler.writeErrors();
    }

    Compiler(String fin,String fout){
        this.reader = Reader.getInstance();
        this.writer = Writer.getInstance();
        this.reader.readerInit(fin);
        this.writer.writerInit(fout);
        this.unit = new CompUnit();
    }

    void writeTokens(){
        Token.writeTokens(writer);
        writer.writeContent();
    }

    void writeTokensVn(){
        unit.writeVnVt(writer);
        writer.writeContent();
    }

    void writeErrors(){
        Error.writeErrors(writer);
        writer.writeContent();
    }
}
