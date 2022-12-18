package error;

import utils.Writer;

import java.util.ArrayList;
import java.util.Collections;

public class Error implements Comparable<Error>{

    int line;
    ErrorType type;

    public static ArrayList<Error> errors = new ArrayList<>();

    public static int addError(Error e){
        boolean flag = true;
        for(Error error:errors){
            if(e.line == error.line && e.type == error.type){
                flag = false;
            }
        }
        if(flag){
            errors.add(e);
        }
        return 0;
    }

    public Error(int line, ErrorType type){
        this.line = line;
        this.type = type;
    }

    public int compareTo(Error e) {
        return ((this.line < e.line) ? (-1)
                : ((this.line == e.line)
                ? 0 : 1));
    }

    public static void error(){
        System.out.println("error.Error");
    }

    public static void error(String msg){
        System.out.println("error.Error: " + msg);
    }

    public static void writeErrors(Writer writer){
        Collections.sort(errors);
        for(Error error:errors){
            writer.addStr(error.line + " " + error.type.getName() + "\n");
        }
    }
}


