package frontend.symbol;

import java.util.ArrayList;

public class Symbol{
    private String name = "";
    private SymbolKind kind;
    private SymbolType type;

    private int arrayDim = 0;
    private SymbolFuncType funcType;
    private int funcFParamNum = 0;
    private ArrayList<Symbol> funcParas = new ArrayList<>();

    private int regindex = 0;
    private int index = 0;

    public Symbol(String symbol, SymbolKind kind){
        this.name = symbol;
        this.kind = kind;
        this.type = type;
    }

    public Symbol(String symbol, SymbolKind kind, SymbolType type){
        this.name = symbol;
        this.kind = kind;
        this.type = type;
    }

    public Symbol(int regindex){
        this.regindex = regindex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRegindex() {
        return regindex;
    }

    public static String reg2str(int regindex){
        return "%" + String.valueOf(regindex);
    }

    public Symbol getFuncPara(int index) {
        if(index >= 0 && index < this.funcParas.size()){
            return this.funcParas.get(index);
        } else {
            return null;
        }
    }

    public void addFuncPara(Symbol symbol){
        this.funcParas.add(symbol);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SymbolKind getKind() {
        return kind;
    }

    public void setKind(SymbolKind kind) {
        this.kind = kind;
    }

    public SymbolType getType(){
        return this.type;
    }

    public void setType(SymbolType type){
        this.type = type;
    }

    public int getArrayDim() {
        return arrayDim;
    }

    public void setArrayDim(int dim){
        this.arrayDim = dim;
    }

    public SymbolFuncType getFuncType() {
        return funcType;
    }

    public void setFuncType(SymbolFuncType funcType) {
        this.funcType = funcType;
    }

    public int getFuncFParamNum() {
        return funcFParamNum;
    }

    public void setFuncFParamNum(int num){
        this.funcFParamNum = num;
    }

    public boolean isArray(){
        if(this.type.equals(SymbolType.ARRAY)){
            return true;
        }
        return false;
    }

    public boolean isKindOf(SymbolKind kind){
        if(this.kind.equals(kind)){
            return true;
        }
        return false;
    }

    public boolean isFunc(){
        return isKindOf(SymbolKind.FUNC);
    }

    public boolean isVar(){
        return isKindOf(SymbolKind.VAR);
    }

    public boolean isConst(){
        return isKindOf(SymbolKind.CONST);
    }

    public boolean isPara() {
        return isKindOf(SymbolKind.PARA);
    }
}
