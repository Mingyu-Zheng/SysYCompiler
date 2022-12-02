package backend;

import utils.Writer;

public class Imm extends MipsIns{
    protected String name = "";
    protected Reg rs = null;
    protected Reg rt = null;
    protected String label = "";
    private String[] branches = {"beq","bne","blt","ble","bgt","bge"};
    private String[] memories = {"lw", "sw"};

    public Imm(String name, Reg rs, Reg rt, String label){
        this.name = name;
        this.rs = rs;
        this.rt = rt;
        this.label = label;
        for(int i = 0;i < branches.length;i++){
            if(name.equals(branches[i])){
                this.label = MipsBlock.getBlockLabel(label);
            }
        }
    }

    public Imm(String name, Reg rs, Reg rt, int offset){
        this.name = name;
        this.rs = rs;
        this.rt = rt;
        this.label = String.valueOf(offset);
    }

    @Override
    public int writeMips(Writer writer) {
        String line = "    " + this.name + " ";
        boolean ismem = false;
        for(int i = 0; i < memories.length;i++){
            if(name.equals(memories[i])){
                ismem = true;
            }
        }
        if(ismem){
            line += this.rt.toString() + ", " + this.label + "(" + this.rs.toString() + ")" + "\n";
        } else {
            line += this.rt.toString() + ", " + this.rs.toString() + ", " + this.label + "\n";
        }
        writer.addStr(line);
        return 0;
    }
}
