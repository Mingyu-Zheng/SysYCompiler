package midend.llvm;

public class InsSrem extends Instruction{
    private String insName = "srem";
    public InsSrem(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }
    public String getInsName() {
        return insName;
    }
}
