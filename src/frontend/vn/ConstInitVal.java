package frontend.vn;

import error.Error;
import frontend.token.Token;
import frontend.token.TokenType;

public class ConstInitVal extends Vn{

    public ConstInitVal(){
        super("<ConstInitVal>");
    }
    public int RConstInitVal(){
        int ret = 0;
        Token token = Token.nextToken();
        if(!token.isType(TokenType.LBRACE)){
            Token.retractToken();
            ConstExp constExp = new ConstExp();
            constExp.RConstExp();
            this.addVn(constExp);
        } else {
            this.addToken(token);
            token = Token.nextToken();
            if(!token.isType(TokenType.RBRACE)){
                Token.retractToken();
                while(true){
                    ConstInitVal constInitVal = new ConstInitVal();
                    constInitVal.RConstInitVal();
                    this.addVn(constInitVal);
                    token = Token.nextToken();
                    if(!token.isType(TokenType.COMMA)){
                        break;
                    }
                    this.addToken(token);
                }
                if(!token.isType(TokenType.RBRACE)){
                    Error.error("<ConstInitVal>");
                    ret = -1;
                }
                this.addToken(token);
            } else {
                if(!token.isType(TokenType.RBRACE)){
                    Error.error("<ConstInitVal>");
                    ret = -1;
                }
                this.addToken(token);
            }
        }
        return ret;
    }
}
