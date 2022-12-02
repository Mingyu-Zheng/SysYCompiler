package midend.llvm;

public class InsBranch extends Instruction{
    BasicBlock targetBlock = null;

    public String getLabel(){
        return this.targetBlock.getBasicname();
    }

    public String getInsName() {
        return insName;
    }
}
