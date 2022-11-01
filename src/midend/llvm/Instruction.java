package midend.llvm;

import utils.Writer;

import java.util.ArrayList;

public class Instruction extends User{

    protected String result = "";
    protected String insName = "";
    protected VarType varType = VarType.INT;
    protected Operator op1 = null;
    protected Operator op2 = null;

    public Instruction(){

    }

    public Instruction(String result, VarType varType, Operator op1, Operator op2){
        this.varType = varType;
        this.op1 = op1;
        this.op2 = op2;
        this.result = result;
    }

    public String getInsName() {
        return insName;
    }

    @Override
    public int writeValue(Writer writer) {
        if(op1 == null || op2 == null){
            return -1;
        }
        String line = "    " + this.result + " = " + this.getInsName() + " " + this.varType.getTypeName()  + " ";
        line += this.op1.printVar() + ", " + this.op2.printVar() + "\n";
        writer.addStr(line);
        return 0;
    }
}


















