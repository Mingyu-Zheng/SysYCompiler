package frontend.symbol;

public enum SymbolType{

    INT("int"),
    ARRAY("array"),
    ;
    private String name;
    private SymbolType(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
