package midend.llvm;

public class InsAdd extends Instruction{
    private String insName = "add";

    public InsAdd(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }
}
