package midend.llvm;

import utils.Writer;

public class InsAdd extends Instruction{
    private String insName = "addu";

    public InsAdd(String result, VarType varType, Operator op1, Operator op2){
        super(result,varType,op1,op2);
    }

    public String getInsName() {
        return insName;
    }
}
