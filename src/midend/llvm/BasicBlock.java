package midend.llvm;

import utils.Writer;

import java.util.ArrayList;

public class BasicBlock extends Value{
    protected String basicname = "";
    protected ArrayList<Instruction> instructions = new ArrayList<>();
    protected ValueFuncDef fatherFunc = null;

    public BasicBlock(){
        super();
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

    public void addNextBlock(BasicBlock block){
        this.fatherFunc.addBasicBlock(block);
    }

    public void addInstruction(Instruction instruction){
        this.instructions.add(instruction);
    }

    public String getBasicname() {
        return basicname;
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
