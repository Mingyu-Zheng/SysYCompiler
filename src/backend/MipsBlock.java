package backend;

import utils.Writer;

import java.util.ArrayList;

public class MipsBlock extends Mips{
    protected String name = "";
    protected ArrayList<MipsIns> mipsInses = new ArrayList<>();

    public MipsBlock(){

    }

    public MipsBlock(String name){
        this.name = "b" + name;
    }

    public static String getBlockLabel(String label){
        return "b" + label;
    }

    public void addIns(MipsIns ins){
        this.mipsInses.add(ins);
    }

    @Override
    public int writeMips(Writer writer) {
        String line = "\n" + this.name + ":\n";
        writer.addStr(line);
        for(MipsIns ins:mipsInses){
            ins.writeMips(writer);
        }
        return 0;
    }
}
