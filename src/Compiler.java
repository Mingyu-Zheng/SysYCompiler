import backend.MipsModule;
import error.Error;
import frontend.vn.CompUnit;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import midend.llvm.ValueModule;
import utils.Reader;
import utils.Writer;

public class Compiler {

    Reader reader;
    Writer writer;

    static CompUnit unit;
    static SymbolTable symbolTableForError;
    static SymbolTable symbolTableForLLVM;
    static ValueModule unitLLVM;
    static MipsModule unitMIPS;

    public static void main(String args[]){
        Compiler compiler = new Compiler("testfile.txt","mips.txt");
        Token token = null;
        while((token = Token.getToken(compiler.reader)) != null){
            Token.addToken(token);
        }
        unit = new CompUnit();
        unit.RCompUnit();
        symbolTableForError = new SymbolTable(1);
        unit.RAnalysis(symbolTableForError);
        symbolTableForLLVM = new SymbolTable(1);
        unitLLVM = new ValueModule();
        unit.RLLVM(symbolTableForLLVM, unitLLVM);
        unitMIPS = new MipsModule();
//        compiler.writeLLVM();
        unitLLVM.RMIPS(unitMIPS);
        compiler.writeMIPS();
    }

    Compiler(String fin,String fout){
        this.reader = Reader.getInstance();
        this.writer = Writer.getInstance();
        this.reader.readerInit(fin);
        this.writer.writerInit(fout);
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

    void writeMIPS(){
        unitMIPS.writeMips(writer);
        writer.writeContent();
    }
}
