package backend;

import utils.Writer;

import java.util.ArrayList;

public class MipsModule extends Mips{
    protected ArrayList<MipsGlobalDef> globalDefs = new ArrayList<>();
    protected ArrayList<MipsFuncDef> funcDefs = new ArrayList<>();
    protected MipsBlock beginBlock = null;

    public MipsModule(){
        this.initBeginBlock();
    }

//    addiu $sp, $sp, -8
//    sw $ra, 0($sp)
//    jal funct_main
//    lw $ra, 0($sp)
//    li $v0, 10
//    syscall
    private void initBeginBlock(){
        this.beginBlock = new MipsBlock();
        this.beginBlock.addIns(new Imm("addiu", Reg.SP, Reg.SP, "-8"));
        this.beginBlock.addIns(new Imm("sw", Reg.SP, Reg.RA,"0"));
        this.beginBlock.addIns(new Jum("jal", "fmain"));
        this.beginBlock.addIns(new Imm("lw", Reg.SP, Reg.RA,"0"));
        this.beginBlock.addIns(new Lai("li", Reg.V0,"10"));
        this.beginBlock.addIns(new Syscall());
    }

    public void addGlobalDef(MipsGlobalDef globalDef){
        this.globalDefs.add(globalDef);
    }

    public void addFuncDef(MipsFuncDef funcDef){
        this.funcDefs.add(funcDef);
    }

    @Override
    public int writeMips(Writer writer) {
        writer.addStr(".data\n");
        for(MipsGlobalDef globalDef:globalDefs){
            globalDef.writeMips(writer);
        }
        writer.addStr("\n.text\n");
        this.beginBlock.writeMips(writer);
        for(MipsFuncDef funcDef:funcDefs){
            funcDef.writeMips(writer);
        }
        return 0;
    }

}
