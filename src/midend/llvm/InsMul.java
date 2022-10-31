package midend.llvm;

public class InsMul extends Instruction{
    private String insName = "mul";

    public InsMul(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }
}
