package midend.llvm;

import utils.Writer;

public class InsLoad extends Instruction{
    private String insName = "load";
    public InsLoad(String result, VarType varType, Operator op1){
        super();
        this.result = result;
        this.varType = varType;
        this.op1 = op1;
    }

    public InsLoad(String result, VarType varType, Operator op1, int i16){
        super();
        this.result = result;
        this.varType = varType;
        this.op1 = op1;
        this.i16 = i16;
    }

    @Override
    public int writeValue(Writer writer) {
        String line = "    " + this.result + " = " + this.insName + " ";
        line += this.varType.getTypeName() + ", " + this.op1.printOperator() + "(" + this.i16 + ")" + "\n";
        writer.addStr(line);
        return 0;
    }
}
