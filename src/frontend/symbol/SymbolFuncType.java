package frontend.symbol;

public enum SymbolFuncType{

    INT("int"),
    VOID("void"),
    ;
    private String name;
    private SymbolFuncType(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public boolean isFuncType(SymbolFuncType type){
        if(this.name.equals(type.name)){
            return true;
        }
        return false;
    }
    public static boolean isFuncType(String name, SymbolFuncType type){
        if(type.name.equals(name)){
            return true;
        }
        return false;
    }

}