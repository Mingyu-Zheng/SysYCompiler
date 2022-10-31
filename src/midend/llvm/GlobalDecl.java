package midend.llvm;

import utils.Writer;

public class GlobalDecl extends Value{
    protected String result = "";
    protected VarType varType = VarType.INT;
    protected int value = 0;
    protected String valuestr = "0";

    public GlobalDecl(String result, VarType varType, int value){
        this.result = result;
        this.varType = varType;
        this.value = value;
        this.valuestr = String.valueOf(value);
    }

    @Override
    public int writeValue(Writer writer) {
        String line = this.result + " = global " + this.varType.getTypeName() + " " + this.valuestr + "\n";
        writer.addStr(line);
        return 0;
    }
}
