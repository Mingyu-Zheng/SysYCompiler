package frontend.vn;

import frontend.symbol.SymbolTable;

public class ConstExp extends Vn{

    public ConstExp(){
        super("<ConstExp>");
    }
    public int RConstExp(){
        AddExp addExp = new AddExp();
        addExp.RAddExp();
        this.addVn(addExp);
        return 0;
    }

    @Override
    public int computeValue(SymbolTable symbolTable) {
        return this.vns.get(0).computeValue(symbolTable);
    }
}
