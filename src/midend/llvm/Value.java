package midend.llvm;

import frontend.token.Token;
import frontend.vn.Vn;
import utils.Writer;

import java.util.ArrayList;

public class Value {
    ArrayList<Value> values = new ArrayList<>();



    public int writeValue(Writer writer){
        for(Value value:values){
            value.writeValue(writer);
        }
        return 0;
    }
}
