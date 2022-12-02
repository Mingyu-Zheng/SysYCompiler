package backend;

import midend.llvm.BasicBlock;
import utils.Writer;

import java.util.ArrayList;

public class MipsFuncDef extends Mips{
    protected int offset = 0;
    protected String name = "";
    protected Imm savera = new Imm("sw",Reg.SP, Reg.RA, -4);
    protected Imm stackaddiu = null;
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

    public void setOffset(int offset){
        this.offset = offset;
        int num = -offset;
        String label = String.valueOf(num);
        stackaddiu = new Imm("addiu", Reg.SP, Reg.SP, label);
    }

    @Override
    public int writeMips(Writer writer) {
        String line = "\n" + this.name + ":\n";
        writer.addStr(line);
        savera.writeMips(writer);
        stackaddiu.writeMips(writer);
        for(MipsBlock block:mipsBlocks){
            block.writeMips(writer);
        }
        return 0;
    }
}
