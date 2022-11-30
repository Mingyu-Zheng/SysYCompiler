package backend;

import midend.llvm.BasicBlock;
import utils.Writer;

import java.util.ArrayList;

public class MipsFuncDef extends Mips{
    protected String name = "";
    protected ArrayList<MipsBlock> mipsBlocks = new ArrayList<>();

    public MipsFuncDef(String name){
        this.name = "f";
        this.name += name.substring(1);
    }

    public static String getFuncLabel(String label){
        return "f" + label.substring(1);
    }

    public void addMipsBlock(MipsBlock mipsBlock){
        this.mipsBlocks.add(mipsBlock);
    }

    @Override
    public int writeMips(Writer writer) {
        String line = this.name + ":\n";
        writer.addStr(line);
        for(MipsBlock block:mipsBlocks){
            block.writeMips(writer);
        }
        return 0;
    }
}
