package midend.llvm;

import utils.Writer;

import java.util.ArrayList;

public class FuncDecl extends User{
    protected VarType varType;
    protected String symbolName = "@";
    protected ArrayList<VarType> typeList = new ArrayList<>();

    public FuncDecl(VarType varType, String symbolName, VarType...varTypes) {
        this.varType = varType;
        this.symbolName = "@" + symbolName;
        for(VarType type:varTypes){
            this.typeList.add(type);
        }
    }

    @Override
    public int writeValue(Writer writer) {
        String line = "declare " + varType.getTypeName() + " " + this.symbolName + "(";
        if(!typeList.isEmpty()){
            line += typeList.get(0).getTypeName();
            for(int i = 1;i < typeList.size(); i++){
                line += ", ";
                line += typeList.get(i).getTypeName();
            }
        }
        line += ")";
        writer.addStr(line);
        return 0;
    }
}
