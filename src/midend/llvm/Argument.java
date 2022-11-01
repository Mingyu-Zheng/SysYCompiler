package midend.llvm;

import java.util.ArrayList;

public class Argument extends Value{
    private ArrayList<Operator> arguments = new ArrayList<>();

    boolean isConst = false;

    public Argument(){

    }

    public void addOperator(Operator operator){
        this.arguments.add(operator);
    }

    public String printArgument(){
        String ret = "";
        if(!arguments.isEmpty()){
            ret += arguments.get(0).printOperator();
            for(int i = 1;i < arguments.size();i++){
                ret += ", ";
                ret += arguments.get(i).printOperator();
            }
        }
        return ret;
    }

    public String printArgumentTypes(){
        String ret = "";
        if(!arguments.isEmpty()){
            ret += arguments.get(0).printType();
            for(int i = 1;i < arguments.size();i++){
                ret += ", ";
                ret += arguments.get(i).printType();
            }
        }
        return ret;
    }
}
