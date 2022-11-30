package backend;

import utils.Writer;

public class Syscall extends MipsIns{
    protected String name = "syscall";
    public Syscall(){

    }

    @Override
    public int writeMips(Writer writer) {
        String line = "    " + this.name + "\n";
        writer.addStr(line);
        return 0;
    }
}
