package midend.llvm;

import utils.Writer;

public class InsStore extends Instruction{
    private String insName = "store";
    public InsStore(VarType varType, Operator op1, Operator op2){
        super();
        this.varType = varType;
        this.op1 = op1;
        this.op2 = op2;
    }

    public InsStore(VarType varType, Operator op1, Operator op2, int i16){
        super();
        this.varType = varType;
        this.op1 = op1;
        this.op2 = op2;
        this.i16 = i16;
    }

    public String getInsName() {
        return insName;
    }

    @Override
    public int writeValue(Writer writer) {
        if(op1 == null || op2 == null){
            return -1;
        }
        String line = "    " + this.insName + " ";
        line += this.op1.printOperator() + ", " + this.op2.printOperator() + "(" + this.i16 + ")" + "\n";
        writer.addStr(line);
        return 0;
    }
}
