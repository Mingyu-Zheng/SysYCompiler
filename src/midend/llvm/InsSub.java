package midend.llvm;

public class InsSub extends Instruction{
    private String insName = "sub";
    public InsSub(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }
}
