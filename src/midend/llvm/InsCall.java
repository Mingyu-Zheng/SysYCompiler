package midend.llvm;

import utils.Writer;

import java.util.ArrayList;

public class InsCall extends Instruction{
    private String insName = "call";
    private String symbolName = "@";
    private ArrayList<Argument> arguments = new ArrayList<>();


    public InsCall(String result, VarType varType, String symbolName, Argument...arguments){
        super();
        this.result = result;
        this.varType = varType;
        this.symbolName = "@" + symbolName;
        for(Argument argument:arguments){
            this.arguments.add(argument);
        }
    }

    @Override
    public int writeValue(Writer writer) {
        String line = "    " + this.result + " = " + this.insName + " " ;
        line += this.varType.getTypeName() + " " + this.symbolName  + "(";
        if(!arguments.isEmpty()){
            line += arguments.get(0).printArgument();
            for(int i = 1; i < arguments.size(); i++){
                line += ", ";
                line += arguments.get(i).printArgument();
            }
        }
        line += ")\n";
        writer.addStr(line);
        return 0;
    }
}
