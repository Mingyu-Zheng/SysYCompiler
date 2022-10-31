package midend.llvm;

import utils.Writer;

import java.util.ArrayList;

public class FuncDef extends User{
    private VarType retType;
    private String symbolName;
    private ArrayList<Operator> operators = new ArrayList<>();
    private ArrayList<BasicBlock> basicBlocks = new ArrayList<>();

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String funcName) {
        this.symbolName = "@" + funcName;
    }

    public VarType getRetType() {
        return retType;
    }

    public void setRetType(VarType retType) {
        this.retType = retType;
    }

    public void addOperator(Operator operator) {
        this.operators.add(operator);
    }

    public void addBasicBlock(BasicBlock basicBlock) {
        this.basicBlocks.add(basicBlock);
    }

    public String printOperators() {
        String ret = "";
        if(operators.isEmpty()){
            return ret;
        } else {
            ret += operators.get(0).printOperator();
            for(int i = 1;i < operators.size(); i++){
                ret += ", ";
                ret += operators.get(i).printOperator();
            }
        }
        return ret;
    }

    @Override
    public int writeValue(Writer writer) {
        String header = "declare" + " " + retType.getTypeName() + " " + this.symbolName + "(" ;
        header += this.printOperators();
        header += ") {\n";
        for(BasicBlock block:basicBlocks){
            block.writeValue(writer);
        }
        writer.addStr("}\n\n");
        return 0;
    }
}
