package frontend.vn;

import error.Error;
import frontend.token.Token;
import frontend.token.TokenType;

public class FuncType extends Vn{

    public FuncType(){
        super("<FuncType>");
    }
    public int RFuncType(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.INTTK)){
            this.addToken(token);
        } else if(token.isType(TokenType.VOIDTK)){
            this.addToken(token);
        } else {
            Error.error("<FuncType>");
            ret = -1;
        }
        return ret;
    }
}
