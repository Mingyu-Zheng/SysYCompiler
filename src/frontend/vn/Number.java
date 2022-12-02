package frontend.vn;

import error.Error;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;

public class Number extends Vn{

    public Number(){
        super("<Number>");
    }
    public int RNumber(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.INTCON)){
            this.addToken(token);
        } else {
            Error.error("<Number>");
            ret = -1;
        }
        return ret;
    }

    @Override
    public int RAnalysis(SymbolTable symbolTable, Symbol symbol) {
        int ret = 0;
        if(symbol.isArray()){
            ret = -2;
        }
        return ret;
    }

    public int getInt(){
        return Integer.parseInt(this.vns.get(0).getToken().getValue());
    }
}