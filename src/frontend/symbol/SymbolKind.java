package frontend.symbol;

public enum SymbolKind{
    VAR("var"),
    CONST("const"),
    FUNC("func"),
    PROC("proc"),
    PARA("para"),
    ;

    private String name;
    private SymbolKind(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public boolean isSymbolKind(SymbolKind symbolKind){
        if(this.name.equals(symbolKind.name)){
            return true;
        }
        return false;
    }
}
