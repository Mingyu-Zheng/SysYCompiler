package frontend.symbol;

import java.util.ArrayList;

public class SymbolTable {
    int depth = 1;
    SymbolTable parentTable = null;
    private ArrayList<Symbol> symbols = new ArrayList<>();
    private ArrayList<SymbolTable> sonTables = new ArrayList<>();

    public SymbolTable(int depth){
        this.depth = depth;
    }

    public SymbolTable(SymbolTable parentTable){
        this.parentTable = parentTable;
        this.depth = parentTable.getDepth() + 1;
    }

    public int getDepth(){
        return this.depth;
    }

    public void addSymbol(Symbol symbol){
        this.symbols.add(symbol);
    }

    public void addSymbolPara(Symbol symbol){
        for(Symbol s:this.symbols){
            symbol.addFuncPara(s);
        }
    }

    public SymbolTable newSonTable(){
        SymbolTable table = new SymbolTable(this);
        this.sonTables.add(table);
        return table;
    }

    public boolean isSymbolExistThis(String str, SymbolKind kind){
        for(Symbol s : symbols){
            if(s.getName().equals(str)){
                if(kind.isSymbolKind(SymbolKind.CONST) || kind.isSymbolKind(SymbolKind.VAR) || kind.isSymbolKind(SymbolKind.PARA)){
                    if(s.isConst() || s.isVar() || s.isPara()){
                        return true;
                    }
                } else if(kind.isSymbolKind(SymbolKind.FUNC)){
                    if(s.isFunc()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isSymbolExist(String str, SymbolKind kind){
        SymbolTable table = this;
        while(table != null){
            if(table.isSymbolExistThis(str, kind)){
                return true;
            }
            table = table.parentTable;
        }
        return false;
    }

    public Symbol getSymbolThis(String name, SymbolKind... kind){
        for(Symbol s : symbols){
            if(s.getName().equals(name)){
                for(SymbolKind thekind : kind){
                    if(s.isKindOf(thekind)){
                        return s;
                    }
                }
            }
        }
        return null;
    }

    public Symbol getSymbol(String name, SymbolKind... kind){
        SymbolTable table = this;
        while(table != null){
            Symbol symbol = table.getSymbolThis(name, kind);
            if(symbol != null){
                return symbol;
            }
            table = table.parentTable;
        }
        return null;
    }
}







