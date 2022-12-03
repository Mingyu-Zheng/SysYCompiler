package midend.llvm;

import utils.Writer;

import java.lang.reflect.Array;

public class ValueGlobalDef extends Value{
    protected String result = "";
    protected VarType varType = VarType.INT;
    protected int value = 0;
    protected String valuestr = "0";
    protected int[] valuearray = null;
    protected boolean isInit = false;

    public ValueGlobalDef(String result, VarType varType, int value){
        this.result = "@" + result;
        this.varType = varType;
        this.value = value;
        this.valuestr = String.valueOf(value);
    }

    public ValueGlobalDef(String result, VarType varType, String str){
        this.result = "@" + result;
        this.varType = varType;
        this.valuestr = str;
    }

    public ValueGlobalDef(String result, VarType varType, boolean isInit,int[] array){
        this.result = "@" + result;
        this.varType = varType;
        this.valuearray = array;
        this.isInit = isInit;
    }

    @Override
    public int writeValue(Writer writer) {
        String line = this.getResult() + " = global " + this.varType.getTypeName() + " ";
        if(this.varType.equals(VarType.ARRAY)){
            if(isInit){
                for(int i = 0;i < this.valuearray.length; i++){
                    line += this.valuearray[i];
                    if(i < this.valuearray.length - 1){
                        line += ", ";
                    } else {
                        line += "\n";
                    }
                }
            } else {
                line += "[" + this.valuearray.length * 4 + "]";
            }
        } else {
            line += this.valuestr + "\n";
        }
        writer.addStr(line);
        return 0;
    }

    public String getResult(){
        return this.result;
    }
}
