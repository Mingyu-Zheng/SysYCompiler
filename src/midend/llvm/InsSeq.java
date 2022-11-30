package midend.llvm;

public class InsSeq extends Instruction{
    private String insName = "seq";

    public InsSeq(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }

    public String getInsName() {
        return insName;
    }


}
