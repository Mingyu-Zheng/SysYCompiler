package midend.llvm;

import backend.Mips;
import frontend.token.Token;
import frontend.vn.Vn;
import midend.mips.StackTable;
import utils.Writer;

import java.util.ArrayList;

public class Value {
    ArrayList<Value> values = new ArrayList<>();


    public int RMIPS(Mips mips){
        return 0;
    }

    public int RMIPS(Mips mips, StackTable table){
        return 0;
    }

    public int writeValue(Writer writer){
        for(Value value:values){
            value.writeValue(writer);
        }
        return 0;
    }
}
