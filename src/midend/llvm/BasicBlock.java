package midend.llvm;

import backend.Mips;
import backend.MipsIns;
import midend.mips.StackTable;
import utils.Writer;

import java.util.ArrayList;

public class BasicBlock extends Value{
    protected String basicname = "";
    protected ArrayList<Instruction> instructions = new ArrayList<>();
    protected ValueFuncDef fatherFunc = null;
    protected BasicBlock breakBlock = null;
    protected BasicBlock continueBlock = null;

    public BasicBlock(){
        super();
    }

    public BasicBlock(ValueFuncDef fatherFunc){
        super();
        this.setFatherFunc(fatherFunc);
    }

    public ValueFuncDef getFatherFunc() {
        return fatherFunc;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public void setFatherFunc(ValueFuncDef fatherFunc) {
        this.fatherFunc = fatherFunc;
        this.basicname = fatherFunc.getPurlName();
        for(int i = 0; i < fatherFunc.basicBlocks.size() ;i++){
            if(fatherFunc.basicBlocks.get(i).equals(this)){
                this.basicname += "_label" + i;
            }
        }
    }

    public BasicBlock getBreakBlock() {
        return breakBlock;
    }

    public void setBreakBlock(BasicBlock breakBlock) {
        this.breakBlock = breakBlock;
    }

    public BasicBlock getContinueBlock() {
        return continueBlock;
    }

    public void setContinueBlock(BasicBlock continueBlock) {
        this.continueBlock = continueBlock;
    }

    public Value getEndBlock(){
        int len = this.fatherFunc.basicBlocks.size();
        return this.fatherFunc.basicBlocks.get(len - 1);
    }

    public void addNextBlock(BasicBlock block){
        this.fatherFunc.addBasicBlock(block);
        if(block.breakBlock == null){
            block.setBreakBlock(this.breakBlock);
        }
        if(block.continueBlock == null){
            block.setContinueBlock(this.continueBlock);
        }
    }

    public void addInstruction(Instruction instruction){
        this.instructions.add(instruction);
    }

    public String getBasicname() {
        return basicname;
    }

    @Override
    public int RMIPS(Mips mips, StackTable table) {
        int ret = 0;

        for(Instruction instruction:instructions){
            ret = instruction.RMIPS(mips, table);
        }

        return ret;
    }

    @Override
    public int writeValue(Writer writer) {
        String line = "\n" + this.basicname + ":\n";
        writer.addStr(line);
        for(Instruction instruction:instructions){
            instruction.writeValue(writer);
        }
        return 0;
    }
}
