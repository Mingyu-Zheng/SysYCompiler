package midend.llvm;

import utils.Writer;

public class InsAlloc extends Instruction {
    private String insName = "alloca";
    private int memSize = 4;

    public InsAlloc(String result, VarType varType){
        super();
        this.result = result;
        this.varType = varType;
    }

    public InsAlloc(String result, VarType varType, int memSize){
        super();
        this.result = result;
        this.varType = varType;
        this.memSize = memSize;
    }

    @Override
    public int writeValue(Writer writer) {
        String line = "    " + this.result + " = " + this.insName + " " +  this.varType.getTypeName() + " " + memSize + "\n";
        writer.addStr(line);
        return 0;
    }
}
