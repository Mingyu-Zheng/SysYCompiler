package frontend.vn;

import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;

public class FuncRParams extends Vn{

    public FuncRParams(){
        super("<frontend.vn.FuncRParams>");
    }
    public int RFuncRParams(){
        int ret = 0;
        Token token = null;
        while(true){
            Exp exp = new Exp();
            exp.RExp();
            this.addVn(exp);
            token = Token.nextToken();
            if(token.isType(TokenType.COMMA)){
                this.addToken(token);
            } else {
                Token.retractToken();
                break;
            }
        }
        return ret;
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