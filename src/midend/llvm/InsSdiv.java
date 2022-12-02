package midend.llvm;

public class InsSdiv extends Instruction{
    private String insName = "div";
    public InsSdiv(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }
    public String getInsName() {
        return insName;
    }
}
