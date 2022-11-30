package backend;

import utils.Writer;

public class Jum extends MipsIns{
    protected String name = "";
    protected String label = "";

    public Jum(String name, String label){
        this.name = name;
        if(name.equals("jal")){
            this.label = MipsFuncDef.getFuncLabel(label);
        } else {
            this.label = MipsBlock.getBlockLabel(label);
        }
    }

    @Override
    public int writeMips(Writer writer) {
        String line = "    " + this.name + " " + this.label + "\n";
        writer.addStr(line);
        return 0;
    }
}
