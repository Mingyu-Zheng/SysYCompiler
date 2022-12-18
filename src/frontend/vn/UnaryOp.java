package frontend.vn;

import error.Error;
import frontend.token.Token;
import frontend.token.TokenType;

public class UnaryOp extends Vn{

    public UnaryOp(){
        super("<UnaryOp>");
    }
    public int RUnaryOp(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.PLUS) || token.isType(TokenType.MINU) || token.isType(TokenType.NOT)){
            this.addToken(token);
        } else {
            Error.error("<UnaryOp>");
            ret = -1;
        }
        return ret;
    }
}
