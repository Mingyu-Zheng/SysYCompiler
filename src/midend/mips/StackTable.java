package midend.mips;


import backend.*;
import midend.llvm.*;

import java.util.ArrayList;

public class StackTable {
    private ValueFuncDef funcDef = null;
    private StackMap stackMap = null;
    private ArrayList<StackItem> stacktable = new ArrayList<>();
    private ArrayList<Reg> allReg = new ArrayList<>();
    private ArrayList<Reg> regs = new ArrayList<>();
    private ArrayList<Reg> reghistory = new ArrayList<>();

    public StackTable(ValueFuncDef funcDef){
        this.funcDef = funcDef;
        this.stackMap = new StackMap(funcDef);
        this.initRegs();
    }

    private void initRegs(){
        Reg.addListAllReg(allReg);
        Reg.addListAllReg(regs);
    }

    public int RMIPS(Mips mips){
        int ret = 0;
        ((MipsFuncDef) mips).setOffset(stackMap.getStacksize());
        ArrayList<BasicBlock> basicBlocks = funcDef.getBasicBlocks();
        for(BasicBlock block:basicBlocks){
            MipsBlock mipsBlock = new MipsBlock(block.getBasicname());
            ArrayList<Instruction> instructions = block.getInstructions();
            for(Instruction instruction:instructions){
                this.addBlockIns(instruction, mipsBlock);
            }
            ((MipsFuncDef) mips).addMipsBlock(mipsBlock);
        }
        return ret;
    }

    public void addGlobals(ArrayList<ValueGlobalDef> globalDefs){
        for(ValueGlobalDef globalDef:globalDefs){
            StackItem stackItem = new StackItem(globalDef.getResult());
            stackItem.setGlobal(true);
            this.stacktable.add(stackItem);
        }
    }

    private void addBlockIns(Instruction ins, MipsBlock mipsBlock){
        String result = ins.getResult();
        if(ins instanceof InsAlloc){
            int offset = stackMap.getOffset(result);
            StackItem stackItem = new StackItem(result);
            stackItem.setPointer(true);
            stackItem.setOffset(offset);
            this.stacktable.add(stackItem);
        } else if(ins instanceof InsStore){
            String rsresult = ins.getOp2Name();
            String rtresult = ins.getOp1Name();
            Reg rt = this.getSymbolReg(rtresult, mipsBlock);
            if(this.isSymbolReg(rsresult, mipsBlock)){
                Reg rs = this.getSymbolReg(rsresult, mipsBlock);
                mipsBlock.addIns(new Imm("sw",rs, rt, ins.getI16()));
            } else {
                int offset = stackMap.getOffset(rsresult) + ins.getI16();
                mipsBlock.addIns(new Imm("sw", Reg.SP, rt, offset));
            }
        } else if(ins instanceof InsLoad){
            String rsresult = ins.getOp1Name();
            String rtresult = ins.getResult();
            int off = 0;
            Reg rs = null;
            Reg rt = this.getSymbolReg(rtresult, mipsBlock);
            if(rsresult.isEmpty()){
                off = Integer.parseInt(ins.getOp1().printVar());
                rs = Reg.ZERO;
                mipsBlock.addIns(new Imm("lw",rs, rt, ins.getI16() + off));
            } else if(this.isSymbolReg(rsresult, mipsBlock)){
                off = stackMap.getOffset(rsresult) + ins.getI16();
                mipsBlock.addIns(new Imm("lw", Reg.SP, rt, off));
            } else {
                rs = this.getSymbolReg(rsresult, mipsBlock);
                mipsBlock.addIns(new Imm("lw",rs, rt, ins.getI16() + off));
            }
        } else if(ins instanceof InsAddi){
            String rsresult = ins.getOp1Name();
            String rtresult = ins.getResult();
            int off = 0;
            Reg rs = null;
            if(rsresult.isEmpty()){
                off = Integer.parseInt(ins.getOp1().printVar());
                rs = Reg.ZERO;
            } else {
                rs = this.getSymbolReg(rsresult, mipsBlock);
            }
            Reg rt = this.getSymbolReg(rtresult, mipsBlock);
            mipsBlock.addIns(new Imm("addi", rs, rt, ins.getI16() + off));
        } else if(ins instanceof Jump){
            mipsBlock.addIns(new Jum("j", ((Jump) ins).getLabel()));
        } else if(ins instanceof InsLa){
            String rtresult = ins.getResult();
            Operator op1 = ins.getOp1();
            Reg rt = this.getSymbolReg(rtresult, mipsBlock);
            mipsBlock.addIns(new Lai(ins.getInsName(),rt,op1.getVarName()));
        } else if(ins instanceof InsLi){
            String rtresult = ins.getResult();
            Operator op1 = ins.getOp1();
            Reg rt = this.getSymbolReg(rtresult, mipsBlock);
            mipsBlock.addIns(new Lai(ins.getInsName(),rt,op1.printVar()));
        } else if((ins instanceof InsAdd) || (ins instanceof InsSub) ||
                (ins instanceof InsSeq) || (ins instanceof InsSne) ||
                (ins instanceof InsSlt) || (ins instanceof InsSle) ||
                (ins instanceof InsSrem)){
            String rdresult = ins.getResult();
            String rsresult = ins.getOp1Name();
            String rtresult = ins.getOp2Name();
            Reg rs = this.getSymbolReg(rsresult, mipsBlock);
            Reg rt = this.getSymbolReg(rtresult, mipsBlock);
            Reg rd = this.getSymbolReg(rdresult, mipsBlock);
            mipsBlock.addIns(new Ins(ins.getInsName(),rs,rt,rd));
        } else if((ins instanceof InsMul) || (ins instanceof InsSdiv)){
            String rdresult = ins.getResult();
            String rsresult = ins.getOp1Name();
            String rtresult = ins.getOp2Name();
            if(rtresult.isEmpty()){
                Reg rs = this.getSymbolReg(rsresult, mipsBlock);
                String offset = ins.getOp2().printVar();
                Reg rd = this.getSymbolReg(rdresult, mipsBlock);
                mipsBlock.addIns(new Imm(ins.getInsName(),rs,rd,offset));
            } else {
                Reg rs = this.getSymbolReg(rsresult, mipsBlock);
                Reg rt = this.getSymbolReg(rtresult, mipsBlock);
                Reg rd = this.getSymbolReg(rdresult, mipsBlock);
                mipsBlock.addIns(new Ins(ins.getInsName(),rs,rt,rd));
            }
        } else if((ins instanceof BranchBeq) || (ins instanceof BranchBne) ||
                (ins instanceof BranchBgez) || (ins instanceof BranchBgtz) ||
                (ins instanceof BranchBlez) || (ins instanceof BranchBltz)){
            String rsresult = ins.getOp1Name();
            String rtresult = ins.getOp2Name();
            Reg rs = this.getSymbolReg(rsresult, mipsBlock);
            Reg rt = this.getSymbolReg(rtresult, mipsBlock);
            String name = "";
            if(ins instanceof BranchBeq){
                name = ((BranchBeq) ins).getInsName();
            } else if (ins instanceof BranchBne){
                name = ((BranchBne) ins).getInsName();
            } else if (ins instanceof BranchBgez){
                name = ((BranchBgez) ins).getInsName();
            } else if (ins instanceof BranchBgtz){
                name = ((BranchBgtz) ins).getInsName();
            } else if (ins instanceof BranchBlez){
                name = ((BranchBlez) ins).getInsName();
            } else if (ins instanceof BranchBltz){
                name = ((BranchBltz) ins).getInsName();
            }
            mipsBlock.addIns(new Imm(name,rs,rt,((InsBranch) ins).getLabel()));
        } else if(ins instanceof InsCall){
            String resymbol = ((InsCall) ins).getSymbolName();
            ArrayList<Operator> operators = ((InsCall) ins).getOperators();
            if(resymbol.equals("@0putint")){
                String intresult = operators.get(0).getVarName();
                Reg rs = this.getSymbolReg(intresult, mipsBlock);
                mipsBlock.addIns(new Imm("addiu", rs, Reg.A0, 0));
                mipsBlock.addIns(new Lai("li", Reg.V0, "1"));
                mipsBlock.addIns(new Syscall());
            } else if(resymbol.equals("@0putstr")){
                String strlabel = operators.get(0).getVarName();
                mipsBlock.addIns(new Lai("la", Reg.A0, strlabel));
                mipsBlock.addIns(new Lai("li", Reg.V0, "4"));
                mipsBlock.addIns(new Syscall());
            } else if(resymbol.equals("@0getint")){
                String intresult = ins.getResult();
                Reg rt = this.getSymbolReg(intresult, mipsBlock);
                mipsBlock.addIns(new Lai("li", Reg.V0, "5"));
                mipsBlock.addIns(new Syscall());
                mipsBlock.addIns(new Imm("addiu", Reg.V0, rt, 0));
            } else {
                int offset = -8;
                for(Operator operator:operators){
                    String rtresult = operator.getVarName();
                    Reg rt = this.getSymbolReg(rtresult, mipsBlock);
                    mipsBlock.addIns(new Imm("sw",Reg.SP,rt,offset));
                    offset = offset - 4;
                }
                this.saveReg(mipsBlock);
                mipsBlock.addIns(new Jum("jal",resymbol));
                if(!((InsCall) ins).isVoid()){
                    String rtresult = ins.getResult();
                    Reg rt = this.getSymbolReg(rtresult, mipsBlock);
                    mipsBlock.addIns(new Imm("addiu",Reg.V0,rt,0));
                }
            }
        } else if(ins instanceof InsRet){
            String ret = ((InsRet) ins).getRetValue();
            if(!ret.isEmpty()){
                Reg rs = this.getSymbolReg(ret, mipsBlock);
                mipsBlock.addIns(new Imm("addiu",rs, Reg.V0,0));
            }
            mipsBlock.addIns(new Imm("lw",Reg.SP,Reg.RA,this.stackMap.getStacksize() - 4));
            mipsBlock.addIns(new Imm("addiu",Reg.SP,Reg.SP,this.stackMap.getStacksize()));
            mipsBlock.addIns(new Jr(Reg.RA));
        }
    }

    private void saveReg(MipsBlock mipsBlock){
        for(StackItem stackItem:stacktable){
            if(stackItem.isReg){
                mipsBlock.addIns(new Imm("sw", Reg.SP, stackItem.getReg(),stackItem.getOffset()));
                stackItem.isReg = false;
                this.regs.add(stackItem.getReg());
            }
        }
    }

    private boolean isSymbolReg(String result, MipsBlock mipsBlock){
        for(StackItem stackItem:stacktable){
            if(stackItem.getResult().equals(result) && stackItem.isReg){
                return true;
            }
        }
        return false;
    }

    private Reg getSymbolReg(String result, MipsBlock mipsBlock){
        Reg ret = null;
        StackItem item = null;
        if(result.equals("0")){
            return Reg.ZERO;
        }
        for(StackItem stackItem:stacktable){
            if(stackItem.getResult().equals(result)){
                item = stackItem;
                break;
            }
        }
        if(item == null){
            ret = blockGetNewReg(mipsBlock);
            item = new StackItem(result);
            item.setOffset(stackMap.getOffset(result));
            item.setReg(ret);
            this.stacktable.add(item);
        } else {
            if(item.isReg){
                ret = item.getReg();
            } else {
                if(item.isPointer){
                    ret = blockGetNewReg(mipsBlock);
                    mipsBlock.addIns(new Imm("addiu", Reg.SP, ret, item.getOffset()));
                } else if(item.isGlobal){
                    ret = blockGetNewReg(mipsBlock);
                    mipsBlock.addIns(new Lai("la",ret, item.getResult()));
                } else {
                    ret = blockGetNewReg(mipsBlock);
                    mipsBlock.addIns(new Imm("lw", Reg.SP, ret, item.getOffset()));
                    item.setReg(ret);
                }
            }
        }
        return ret;
    }

    private Reg blockGetNewReg(MipsBlock mipsBlock){
        Reg ret = null;
        if(this.regs.size() > 0){
            ret = this.regs.get(0);
            this.regs.remove(0);
            this.reghistory.add(ret);
        } else {
            ret = this.reghistory.get(0);
            for(StackItem stackItem:stacktable){
                if(stackItem.isReg && stackItem.getReg().equals(ret)){
                    mipsBlock.addIns(new Imm("sw", Reg.SP, stackItem.getReg(),stackItem.getOffset()));
                    stackItem.isReg = false;
                    break;
                }
            }
            this.reghistory.remove(0);
            this.reghistory.add(ret);
        }
        return ret;
    }

}
