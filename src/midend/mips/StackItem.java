package midend.mips;

import backend.Reg;

public class StackItem {
    private String result = "";

    protected boolean isReg = false;
    private Reg reg = null;
    protected boolean isPointer = false;
    protected boolean isGlobal = false;

    private int offset = 0;
    private int size = 0;

    public StackItem(String result, int size){
        this.result = result;
        this.size = size;
        this.reg = null;
        this.isReg = false;
    }

    public StackItem(String result){
        this.result = result;
        this.reg = null;
        this.isReg = false;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public boolean isPointer() {
        return isPointer;
    }

    public void setPointer(boolean pointer) {
        isPointer = pointer;
    }

    public String getResult() {
        return result;
    }

    public int getSize() {
        return size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Reg getReg() {
        return reg;
    }

    public void setReg(Reg reg) {
        this.reg = reg;
        this.isReg = true;
    }
}
