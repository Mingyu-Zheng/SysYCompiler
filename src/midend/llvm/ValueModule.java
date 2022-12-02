package midend.llvm;

import backend.Mips;
import backend.MipsFuncDef;
import backend.MipsGlobalDef;
import backend.MipsModule;
import midend.mips.StackItem;
import midend.mips.StackTable;
import utils.Writer;

import java.util.ArrayList;

public class ValueModule extends Value{
    protected ArrayList<FuncDecl> funcDecls = new ArrayList<>();
    protected ArrayList<ValueGlobalDef> globalDecls = new ArrayList<>();
    protected ArrayList<ValueFuncDef> funcDefs = new ArrayList<>();

    public ValueModule(){
//        FuncDecl getint = new FuncDecl(VarType.INT,"getint");
//        FuncDecl putint = new FuncDecl(VarType.VOID, "putint", VarType.INT);
//        FuncDecl putch = new FuncDecl(VarType.VOID, "putch", VarType.INT);
//        this.funcDecls.add(getint);
//        this.funcDecls.add(putint);
//        this.funcDecls.add(putch);
    }

    public void addGlobalDecl(ValueGlobalDef globalDecl){
        this.globalDecls.add(globalDecl);
    }

    public void addFuncDecl(FuncDecl funcDecl){
        this.funcDecls.add(funcDecl);
    }

    public void addFuncDef(ValueFuncDef funcDef){
        this.funcDefs.add(funcDef);
    }

    @Override
    public int RMIPS(Mips mips) {
        int ret = 0;
        for(ValueGlobalDef globalDecl:globalDecls){
            MipsGlobalDef globalDef = new MipsGlobalDef(globalDecl.result,globalDecl.varType,
                    globalDecl.valuestr,globalDecl.valuearray);
            ((MipsModule) mips).addGlobalDef(globalDef);
        }
        for(ValueFuncDef funcDef:funcDefs){
            MipsFuncDef func = new MipsFuncDef(funcDef.getSymbolName());
            StackTable stackTable = new StackTable(funcDef);
            stackTable.addGlobals(this.globalDecls);
            ret = funcDef.RMIPS(func, stackTable);
            ((MipsModule) mips).addFuncDef(func);
        }
        return ret;
    }

    @Override
    public int writeValue(Writer writer) {
        for(FuncDecl funcDecl:funcDecls){
            funcDecl.writeValue(writer);
        }
        writer.addStr("\n");
        for(ValueGlobalDef globalDecl:globalDecls){
            globalDecl.writeValue(writer);
        }
        writer.addStr("\n");
        for(ValueFuncDef funcDef:funcDefs){
            funcDef.writeValue(writer);
        }
        return 0;
    }
}
