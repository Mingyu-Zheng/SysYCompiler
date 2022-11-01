package midend.llvm;

public class Operator extends User{
    private VarType varType;
    private String varName;
    private int varValue = 0;

    boolean isConst = false;

    public Operator(VarType varType, String varName){
        this.varType = varType;
        this.varName = varName;
    }

    public Operator(VarType varType, int varValue){
        this.varType = varType;
        this.varValue = varValue;
        this.isConst = true;
    }

    public String printOperator(){
        String ret = this.varType.getTypeName() + " ";
        if(this.isConst){
            ret += String.valueOf(this.varValue);
        } else {
            ret += this.varName;
        }
        return ret;
    }

    public String printType(){
        return this.varType.getTypeName();
    }

    public String printVar(){
        String ret = "";
        if(this.isConst){
            ret += String.valueOf(this.varValue);
        } else {
            ret += this.varName;
        }
        return ret;
    }

    public void setConst() {
        this.isConst = true;
    }

    public boolean isConst() {
        return this.isConst;
    }
}
