package midend.llvm;

import utils.Writer;

public class Jump extends Instruction{
    private String insName = "jump";
    BasicBlock targetBlock = null;

    public Jump(BasicBlock targetBlock){
        super();
        this.targetBlock = targetBlock;
    }

    public String getLabel(){
        return this.targetBlock.getBasicname();
    }

    public String getInsName() {
        return insName;
    }

    @Override
    public int writeValue(Writer writer) {
        String line = "    " + this.insName + " ";
        line += targetBlock.getBasicname() + "\n";
        writer.addStr(line);
        return 0;
    }
}
