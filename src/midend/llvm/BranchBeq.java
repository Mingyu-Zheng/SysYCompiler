package midend.llvm;

import utils.Writer;

public class BranchBeq extends InsBranch{
    private String insName = "beq";
    BasicBlock targetBlock = null;

    public BranchBeq(Operator op1, Operator op2, BasicBlock targetBlock){
        super();
        this.op1 = op1;
        this.op2 = op2;
        this.targetBlock = targetBlock;
    }

    public String getLabel(){
        return this.targetBlock.getBasicname();
    }

    @Override
    public int writeValue(Writer writer) {
        String line = "    " + this.insName + " ";
        line += this.op1.printVar() + ", " + this.op2.printVar() + " " +  targetBlock.getBasicname() + "\n";
        writer.addStr(line);
        return 0;
    }

    public String getInsName() {
        return insName;
    }
}
