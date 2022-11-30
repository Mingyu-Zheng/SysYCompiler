package midend.llvm;

public class InsSne extends Instruction{
    private String insName = "sne";

    public InsSne(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }

    public String getInsName() {
        return insName;
    }


}
