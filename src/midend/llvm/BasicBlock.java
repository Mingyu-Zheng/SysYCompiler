package midend.llvm;

import utils.Writer;

import java.util.ArrayList;

public class BasicBlock extends Value{
    protected ArrayList<Instruction> instructions = new ArrayList<>();

    public void addInstruction(Instruction instruction){
        this.instructions.add(instruction);
    }

    @Override
    public int writeValue(Writer writer) {
        for(Instruction instruction:instructions){
            instruction.writeValue(writer);
        }
        return 0;
    }
}
