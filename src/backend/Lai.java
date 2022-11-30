package backend;

import utils.Writer;

public class Lai extends MipsIns{
    protected String name = "";
    protected Reg rt = null;
    protected String label = "";

    public Lai(String name, Reg rt, String label){
        this.name = name;
        this.rt = rt;
        if(name.equals("la")){
            this.label = MipsGlobalDef.getGlobalLabel(label);
        } else {
            this.label = label;
        }
    }

    @Override
    public int writeMips(Writer writer) {
        String line = "    " + this.name + " " + this.rt.toString() + ", " + this.label + "\n";
        writer.addStr(line);
        return 0;
    }
}
