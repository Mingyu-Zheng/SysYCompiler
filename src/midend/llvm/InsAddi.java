package midend.llvm;

import utils.Writer;

public class InsAddi extends Instruction{
    private String insName = "addi";

    public InsAddi(String result, VarType varType, Operator op1, int i16){
        this.result = result;
        this.varType = varType;
        this.op1 = op1;
        this.i16 = i16;
    }

    public String getInsName() {
        return insName;
    }

    @Override
    public int writeValue(Writer writer) {
        String line = "    " + this.result + " = " + this.getInsName() + " " + this.varType.getTypeName()  + " ";
        line += this.op1.printVar() + ", " + this.i16 + "\n";
        writer.addStr(line);
        return 0;
    }
}
