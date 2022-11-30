package midend.llvm;

import backend.Mips;
import backend.MipsBlock;
import backend.MipsFuncDef;
import midend.mips.StackTable;
import utils.Writer;

import java.util.ArrayList;

public class ValueFuncDef extends User{
    private VarType retType;
    private String symbolName;
    private Argument argument;
    protected ArrayList<BasicBlock> basicBlocks = new ArrayList<>();

    public ValueFuncDef(VarType retType, String symbolName){
        this.retType = retType;
        this.symbolName = "@" + symbolName;
        this.argument = new Argument();
    }

    public String getSymbolName() {
        return symbolName;
    }

    public String getPurlName() {
        String str = this.symbolName.substring(1);
        return str;
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
        basicBlock.setFatherFunc(this);
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
    public int RMIPS(Mips mips) {
        int ret = 0;
        StackTable stackTable = new StackTable();

        for(BasicBlock basicBlock:basicBlocks){
            MipsBlock block = new MipsBlock(basicBlock.basicname);
            ((MipsFuncDef) mips).addMipsBlock(block);
            ret = basicBlock.RMIPS(block,stackTable);
        }
        return ret;
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
