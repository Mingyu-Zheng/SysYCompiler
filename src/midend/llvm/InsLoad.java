package midend.llvm;

public class InsLoad extends Instruction{
    private String insName = "load";
    public InsLoad(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }
}
