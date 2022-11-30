package backend;

import utils.Writer;

public class Ins extends MipsIns{
    String name = "";
    Reg rs = null;
    Reg rt = null;
    Reg rd = null;

    public Ins(String name, Reg rs, Reg rt, Reg rd){
        this.name = name;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
    }

    @Override
    public int writeMips(Writer writer) {
        String line = "    " + this.name + " " + this.rs.toString() + ", " + this.rt.toString() + ", ";
        line += this.rd.toString() + "\n";
        writer.addStr(line);
        return 0;
    }
}
