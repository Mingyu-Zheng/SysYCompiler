package midend.llvm;

public class Argument extends Value{
    private VarType varType;
    private String varName;
    private int varValue;

    boolean isConst = false;

    public Argument(VarType varType, String varName){
        this.varType = varType;
        this.varName = varName;
    }

    public Argument(VarType varType, int varValue){
        this.varType = varType;
        this.varValue = varValue;
        this.isConst = true;
    }

    public String printArgument(){
        String ret = this.varType.getTypeName() + " ";
        if(this.isConst){
            ret += String.valueOf(this.varValue);
        } else {
            ret += this.varName;
        }
        return ret;
    }
}
