package midend.llvm;

public class InsSlt extends Instruction{

    private String insName = "slt";

    public InsSlt(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }

    public String getInsName() {
        return insName;
    }

}
