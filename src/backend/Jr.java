package backend;

import utils.Writer;

public class Jr extends MipsIns{
    protected String name = "jr";
    protected Reg rs = null;

    public Jr(Reg rs){
        this.rs = rs;
    }

    @Override
    public int writeMips(Writer writer) {
        String line = "    " + this.name + " " + this.rs.toString() + "\n";
        writer.addStr(line);
        return 0;
    }
}
