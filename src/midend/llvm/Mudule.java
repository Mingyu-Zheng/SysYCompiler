package midend.llvm;

import jdk.nashorn.internal.ir.FunctionCall;
import utils.Writer;

import java.util.ArrayList;

public class Mudule extends Value{
    protected ArrayList<FuncDecl> funcDecls = new ArrayList<>();
    protected ArrayList<GlobalDecl> globalDecls = new ArrayList<>();
    protected ArrayList<FuncDef> funcDefs = new ArrayList<>();

    public Mudule(){
        FuncDecl getint = new FuncDecl(VarType.INT,"getint");
        FuncDecl putint = new FuncDecl(VarType.VOID, "putint", VarType.INT);
        FuncDecl putch = new FuncDecl(VarType.VOID, "putch", VarType.INT);
        this.funcDecls.add(getint);
        this.funcDecls.add(putint);
        this.funcDecls.add(putch);
    }

    public void addGlobalDecl(GlobalDecl globalDecl){
        this.globalDecls.add(globalDecl);
    }

    public void addFuncDecl(FuncDecl funcDecl){
        this.funcDecls.add(funcDecl);
    }

    @Override
    public int writeValue(Writer writer) {
        for(FuncDecl funcDecl:funcDecls){
            funcDecl.writeValue(writer);
        }
        writer.addStr("\n");
        for(GlobalDecl globalDecl:globalDecls){
            globalDecl.writeValue(writer);
        }
        writer.addStr("\n");
        for(FuncDef funcDef:funcDefs){
            funcDef.writeValue(writer);
        }
        return 0;
    }
}
