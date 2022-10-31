package error;

public enum ErrorType {
    ILLEGAL_SYMBOL("a"),
    NAME_REDEFINE("b"),
    NAME_UNDEFINE("c"),
    FUNCPARA_NUMBER("d"),
    FUNCPARA_TYPE("e"),
    FUNCVOID_RETURN("f"),
    FUNCNOVOID_NORETURN("g"),
    CONST_CHANGE("h"),
    LACK_SEMI("i"),
    LACK_RPARENT("j"),
    LACK_RBRACK("k"),
    PRINTF_NUMBER("l"),
    NOLOOP_BREAKORCONTINUE("m")
    ;



    private String name;
    private ErrorType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
