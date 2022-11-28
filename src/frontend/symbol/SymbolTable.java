package frontend.symbol;

import java.util.ArrayList;

public class SymbolTable {
    int depth = 1;
    SymbolTable parentTable = null;
    private ArrayList<Symbol> symbols = new ArrayList<>();
    private ArrayList<SymbolTable> sonTables = new ArrayList<>();

    private int regnum = -1;
    private int symbolnum = 0;
    private int strnum = 0;

    public ArrayList<Symbol> getSymbols(){
        return this.symbols;
    }

    public SymbolTable(int depth){
        this.depth = depth;
    }

    public SymbolTable(SymbolTable parentTable){
        this.parentTable = parentTable;
        this.depth = parentTable.getDepth() + 1;
        this.regnum = -1;
        this.symbolnum = parentTable.symbolnum;
    }

    public int getDepth(){
        return this.depth;
    }

    public int getStrnum(){
        SymbolTable st = this;
        while (st.parentTable != null){
            st = st.parentTable;
        }
        return st.strnum;
    }

    public void addStrnum(){
        SymbolTable st = this;
        while (st.parentTable != null){
            st = st.parentTable;
        }
        st.strnum++;
    }

    public int getSymbolnum() {
        return symbolnum;
    }

    public void setFuncRegNum(){
        this.regnum++;
    }

    public int newReg() {
        regnum++;
        Symbol symbol = new Symbol(regnum);
        symbol.setIndex(symbolnum);
        this.symbols.add(symbol);
        return symbolnum++;
    }

    public int newMemReg() {
        regnum++;
        Symbol symbol = new Symbol(regnum);
        symbol.setIndex(symbolnum);
        symbol.setIsRegPointer();
        this.symbols.add(symbol);
        return symbolnum++;
    }

    public String getRegByIndex(int index){
        Symbol out = null;
        SymbolTable table = this;
        while(table != null){
            out = table.getRegbyIndexThis(index);
            if(out != null){
                break;
            }
            table = table.parentTable;
        }
        if(out == null){
            return "";
        }
        if(out.getRegindex() == -1){
            return "@" + out.getName();
        } else {
            return "%" + out.getRegindex();
        }
    }

    public void pushOnParaIndex(int oldindex, int newindex){
        Symbol symbolold = this.getRegbyIndexThis(oldindex);
        Symbol symbolnew = this.getRegbyIndexThis(newindex);
        int newregindex = symbolnew.getRegindex();
        symbolold.setIndex(newindex);
        symbolold.setRegindex(newregindex);
        symbolold.setIsRegPointer();
        this.symbols.remove(symbolnew);
    }

    public boolean isIndexPointer(int index){
        SymbolTable table = this;
        Symbol out = null;
        while(table != null){
            out = table.getRegbyIndexThis(index);
            if(out != null){
                return out.isRegPointer();
            }
            table = table.parentTable;
        }
        return false;
    }

    public Symbol getRegbyIndexThis(int index){
        Symbol out = null;
        for(Symbol symbol: symbols){
            if(symbol.getIndex() == index){
                out = symbol;
                break;
            }
        }
        return out;
    }

    public void addSymbol(Symbol symbol){
        symbol.setIndex(symbolnum++);
        this.symbols.add(symbol);
    }

//    public void addreg(){
//        this.regnum++;
//    }

    public void addSymbol2Global(Symbol symbol){
        symbol.setIndex(symbolnum++);
        symbol.setRegindex(-1);
        symbol.setIsRegPointer();
        this.symbols.add(symbol);
    }

    public void addSymbol2Reg(Symbol symbol){
        symbol.setIndex(symbolnum++);
        this.regnum++;
        symbol.setRegindex(regnum);
        this.symbols.add(symbol);
    }

    public void addSymbolMem2Reg(Symbol symbol){
        symbol.setIndex(symbolnum++);
        this.regnum++;
        symbol.setRegindex(regnum);
        symbol.setIsRegPointer();
        this.symbols.add(symbol);
    }

    public void addSymbolPara(Symbol symbol){
        for(Symbol s:this.symbols){
            symbol.addFuncPara(s);
        }
    }

    public void addSymbol2Root(Symbol symbol){
        SymbolTable st = this;
        while(st.parentTable != null){
            st = st.parentTable;
        }
        st.addSymbol(symbol);
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







