package midend.llvm;

import utils.Writer;

import java.util.ArrayList;

public class InsCall extends Instruction{
    private String insName = "call";
    private String symbolName = "@";
    private ArrayList<Operator> operators = new ArrayList<>();

    public String getSymbolName() {
        return this.symbolName;
    }

    public InsCall(String result, VarType varType, String symbolName, Operator...arguments){
        super();
        this.result = result;
        this.varType = varType;
        this.symbolName = "@" + symbolName;
        for(Operator operator:arguments){
            this.operators.add(operator);
        }
    }

    public InsCall(VarType varType, String symbolName, Operator...arguments){
        super();
        this.varType = varType;
        this.symbolName = "@" + symbolName;
        for(Operator operator:arguments){
            this.operators.add(operator);
        }
    }


    @Override
    public int writeValue(Writer writer) {
        String line = "    ";
        if(!this.varType.equals(VarType.VOID)){
            line += this.result + " = ";
        }
        line += this.insName + " " ;
        line += this.varType.getTypeName() + " " + this.getSymbolName()  + "(";
        if(!operators.isEmpty()){
            line += operators.get(0).printOperator();
            for(int i = 1; i < operators.size(); i++){
                line += ", ";
                line += operators.get(i).printOperator();
            }
        }
        line += ")\n";
        writer.addStr(line);
        return 0;
    }
}
