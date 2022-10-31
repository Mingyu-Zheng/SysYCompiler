import error.Error;
import frontend.vn.CompUnit;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import midend.llvm.Mudule;
import utils.Reader;
import utils.Writer;

public class Compiler {

    Reader reader;
    Writer writer;

    static CompUnit unit;
    static SymbolTable symbolTableForError;
    static SymbolTable symbolTableForLLVM;
    static Mudule unitLLVM;

    public static void main(String args[]){
        Compiler compiler = new Compiler("testfile.txt","llvm_ir.txt");
        Token token = null;
        while((token = Token.getToken(compiler.reader)) != null){
            Token.addToken(token);
        }
        unit = new CompUnit();
        unit.RCompUnit();
        symbolTableForError = new SymbolTable(1);
        unit.RAnalysis(symbolTableForError);
        symbolTableForLLVM = new SymbolTable(1);
        unitLLVM = new Mudule();
        unit.RLLVM(symbolTableForLLVM, unitLLVM);
        compiler.writeLLVM();
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

    void writeLLVM(){
        unitLLVM.writeValue(writer);
        writer.writeContent();
    }
}
