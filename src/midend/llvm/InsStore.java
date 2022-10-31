package midend.llvm;

public class InsStore extends Instruction{
    private String insName = "store";
    public InsStore(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }
}
