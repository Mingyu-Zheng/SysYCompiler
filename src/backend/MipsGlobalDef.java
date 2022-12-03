package backend;

import midend.llvm.VarType;
import utils.Writer;

import java.lang.reflect.Array;

public class MipsGlobalDef extends Mips{
    protected String name = "";
    protected int kind = 0;
    protected String valuestr = "";
    protected int[] valuearr = null;
    private boolean isarr = false;
    private String[] kinds = {".word", ".asciiz", ".space"};

    public MipsGlobalDef(String name, VarType type, String valuestr, boolean isInit, int[] valuearr){
        this.name = "g" + name.substring(1);
        if(type.equals(VarType.STR)){
            this.kind = 1;
        } else {
            this.kind = 0;
            if(type.equals(VarType.ARRAY)){
                this.isarr = true;
                if(!isInit){
                    this.kind = 2;
                }
            }
        }
        this.valuestr = valuestr;
        this.valuearr = valuearr;
    }

    public static String getGlobalLabel(String label){
        return "g" + label.substring(1);
    }

    @Override
    public int writeMips(Writer writer) {
        String line = "    " + this.name + ": " + this.kinds[this.kind] + " ";
        if(this.kind == 0 && this.isarr == true){
            for(int i = 0;i < this.valuearr.length; i++){
                line += this.valuearr[i];
                if(i < this.valuearr.length - 1){
                    line += ", ";
                } else {
                    line += "\n";
                }
            }
        } else if(this.kind == 2){
            line += this.valuearr.length * 4 + "\n";
        } else {
            line += this.valuestr + "\n";
        }
        writer.addStr(line);
        return 0;
    }
}
