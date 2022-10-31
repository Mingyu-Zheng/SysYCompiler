package midend.llvm;

import java.util.ArrayList;

public class BasicBlock extends Value{
    private ArrayList<Instruction> instructions = new ArrayList<>();

    public void addInstruction(Instruction instruction){
        this.instructions.add(instruction);
    }
}
