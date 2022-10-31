package frontend.vn;

import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;

public class Exp extends Vn{

    public Exp(){
        super("<frontend.vn.Exp>");
    }
    public int RExp(){
        AddExp addExp = new AddExp();
        addExp.RAddExp();
        this.addVn(addExp);
        return 0;
    }

    @Override
    public int RAnalysis(SymbolTable symbolTable, Symbol symbol) {
        int ret = 0;
        for(Vn vn:vns){
            if(vn.isVt){
                continue;
            } else {
                int retmp = vn.RAnalysis(symbolTable, symbol);
                if(retmp < 0){
                    ret = retmp;
                }
            }
        }
        return ret;
    }
}
