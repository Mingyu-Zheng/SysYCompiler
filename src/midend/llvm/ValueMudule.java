package midend.llvm;

import utils.Writer;

import java.util.ArrayList;

public class ValueMudule extends Value{
    protected ArrayList<FuncDecl> funcDecls = new ArrayList<>();
    protected ArrayList<ValueGlobalDecl> globalDecls = new ArrayList<>();
    protected ArrayList<ValueFuncDef> funcDefs = new ArrayList<>();

    public ValueMudule(){
        FuncDecl getint = new FuncDecl(VarType.INT,"getint");
        FuncDecl putint = new FuncDecl(VarType.VOID, "putint", VarType.INT);
        FuncDecl putch = new FuncDecl(VarType.VOID, "putch", VarType.INT);
        this.funcDecls.add(getint);
        this.funcDecls.add(putint);
        this.funcDecls.add(putch);
    }

    public void addGlobalDecl(ValueGlobalDecl globalDecl){
        this.globalDecls.add(globalDecl);
    }

    public void addFuncDecl(FuncDecl funcDecl){
        this.funcDecls.add(funcDecl);
    }

    public void addFuncDef(ValueFuncDef funcDef){
        this.funcDefs.add(funcDef);
    }

    @Override
    public int writeValue(Writer writer) {
        for(FuncDecl funcDecl:funcDecls){
            funcDecl.writeValue(writer);
        }
        writer.addStr("\n");
        for(ValueGlobalDecl globalDecl:globalDecls){
            globalDecl.writeValue(writer);
        }
        writer.addStr("\n");
        for(ValueFuncDef funcDef:funcDefs){
            funcDef.writeValue(writer);
        }
        return 0;
    }
}
