package midend.llvm;

public class InsSle extends Instruction{
    private String insName = "sle";

    public InsSle(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }

    public String getInsName() {
        return insName;
    }


}