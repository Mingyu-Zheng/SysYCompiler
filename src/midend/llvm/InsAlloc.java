package midend.llvm;

import utils.Writer;

public class InsAlloc extends Instruction {
    private String insName = "alloca";

    public InsAlloc(String result, VarType varType){
        super();
        this.result = result;
        this.varType = varType;
    }

    @Override
    public int writeValue(Writer writer) {
        String line = "    " + this.result + " = " + this.insName + this.varType.getTypeName()  + " ";
        writer.addStr(line);
        return 0;
    }
}
