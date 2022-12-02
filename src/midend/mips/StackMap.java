package midend.mips;

import midend.llvm.BasicBlock;
import midend.llvm.InsAlloc;
import midend.llvm.Instruction;
import midend.llvm.ValueFuncDef;

import java.util.ArrayList;

public class StackMap {

    ValueFuncDef funcDef = null;
    ArrayList<StackItem> stackItems = new ArrayList<>();
    int stacksize = 0;

    public StackMap(ValueFuncDef funcDef){
        this.funcDef = funcDef;
        this.initStackMap(funcDef);
    }

    private void initStackMap(ValueFuncDef funcDef){
        this.stacksize = 0;
        stackItems.add(new StackItem("return",4));
        stacksize += 4;
        ArrayList<BasicBlock> basicBlocks = funcDef.getBasicBlocks();
        for(BasicBlock block:basicBlocks){
            ArrayList<Instruction> instructions = block.getInstructions();
            for(Instruction ins:instructions){
                if(ins.getResult().isEmpty()){
                    continue;
                } else {
                    int size = 4;
                    String result = ins.getResult();
                    if(ins instanceof InsAlloc){
                        size = ((InsAlloc) ins).getMemSize();
                    }
                    StackItem stackItem = new StackItem(result, size);
                    this.stackItems.add(stackItem);
                    stacksize += size;
                }
            }
        }
        int offset = stacksize;
        for(int i = 0;i < stackItems.size();i++){
            StackItem stackItem = stackItems.get(i);
            offset = offset - stackItem.getSize();
            stackItem.setOffset(offset);
        }
    }

    public int getStacksize() {
        return stacksize;
    }

    public int getOffset(String result){
        for(StackItem stackItem:stackItems){
            if(stackItem.getResult().equals(result)){
                return stackItem.getOffset();
            }
        }
        return 0;
    }
}
