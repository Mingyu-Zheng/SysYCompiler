package midend.llvm;

import utils.Writer;

public class InsRet extends Instruction{
    private String insName = "ret";

    private VarType varType;
    private String retVal;
    private int retValue;
    private boolean isConst = false;

    public InsRet(VarType varType, String retVal){
        super();
        this.varType = varType;
        this.retVal = retVal;
    }


    @Override
    public int writeValue(Writer writer) {
        String line = "    " + this.insName + " " + this.varType.getTypeName();
        if(this.varType.equals(VarType.INT)){
            line += " " + this.getRetValue();
        }
        line += "\n";
        writer.addStr(line);
        return 0;
    }

    public String getRetValue() {
        if(this.isConst){
            return String.valueOf(this.retValue);
        } else {
            return this.retVal;
        }
    }
}
