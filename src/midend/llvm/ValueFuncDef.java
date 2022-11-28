package midend.llvm;

import utils.Writer;

import java.util.ArrayList;

public class ValueFuncDef extends User{
    private VarType retType;
    private String symbolName;
    private Argument argument;
    private ArrayList<BasicBlock> basicBlocks = new ArrayList<>();

    public ValueFuncDef(VarType retType, String symbolName){
        this.retType = retType;
        this.symbolName = "@" + symbolName;
        this.argument = new Argument();
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String funcName) {
        this.symbolName = funcName;
    }

    public VarType getRetType() {
        return retType;
    }

    public void setRetType(VarType retType) {
        this.retType = retType;
    }

    public Argument getArgument(){
        return this.argument;
    }

    public void addBasicBlock(BasicBlock basicBlock) {
        this.basicBlocks.add(basicBlock);
    }

    public void checkIsVoidRet(){
        int i = this.basicBlocks.size() - 1;
        boolean flag = false;
        BasicBlock block = this.basicBlocks.get(i);
        for(Instruction instruction:block.instructions){
            if(instruction instanceof InsRet){
                flag = true;
                break;
            }
        }
        if(!flag){
            block.addInstruction(new InsRet(VarType.VOID,""));
        }
    }

    @Override
    public int writeValue(Writer writer) {
        String header = "define" + " " + retType.getTypeName() + " " + this.getSymbolName() + "(" ;
        header += this.argument.printArgumentTypes();
        header += ") {\n";
        writer.addStr(header);
        for(BasicBlock block:basicBlocks){
            block.writeValue(writer);
        }
        writer.addStr("}\n\n");
        return 0;
    }
}
