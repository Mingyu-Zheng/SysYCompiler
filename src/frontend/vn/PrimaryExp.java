package frontend.vn;

import error.Error;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;

public class PrimaryExp extends Vn{

    public PrimaryExp(){
        super("<frontend.vn.PrimaryExp>");
    }
    public int RPrimaryExp(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.LPARENT)){
            this.addToken(token);
            Exp exp = new Exp();
            exp.RExp();
            this.addVn(exp);
            token = Token.nextToken();
            if(token.isType(TokenType.RPARENT)){
                this.addToken(token);
            } else {
                Error.error("<frontend.vn.PrimaryExp>");
                ret = -1;
            }
        } else if(token.isType(TokenType.IDENFR)){
            Token.retractToken();
            LVal lVal = new LVal();
            lVal.RLVal();
            this.addVn(lVal);
        } else if(token.isType(TokenType.INTCON)){
            Token.retractToken();
            Number number = new Number();
            number.RNumber();
            this.addVn(number);
        } else {
            Error.error("<frontend.vn.PrimaryExp>");
            ret = -1;
        }
        return ret;
    }

    @Override
    public int RAnalysis(SymbolTable symbolTable, Symbol symbol) {
        int ret = 0;
        Vn vn0 = this.vns.get(0);
        if(vn0.isVt){
            if(symbol.isArray()){
                ret = -2;
            } else {
                if(vn0.getToken().isType(TokenType.LPARENT)){
                    for(Vn vn:vns){
                        if(!vn.isVt){
                            int retmp = vn.RAnalysis(symbolTable,symbol);
                            if(retmp < 0){
                                ret = retmp;
                            }
                        }
                    }
                }
            }

        } else {
            ret = vn0.RAnalysis(symbolTable,symbol);
        }
        return ret;
    }
}
