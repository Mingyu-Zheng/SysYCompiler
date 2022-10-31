package frontend.vn;

import error.Error;
import frontend.token.Token;
import frontend.token.TokenType;

public class InitVal extends Vn{

    public InitVal(){
        super("<frontend.vn.InitVal>");
    }
    public int RInitVal(){
        int ret = 0;
        Token token = Token.nextToken();
        if(!token.isType(TokenType.LBRACE)){
            Token.retractToken();
            Exp exp = new Exp();
            exp.RExp();
            this.addVn(exp);
        } else {
            this.addToken(token);
            token = Token.nextToken();
            if(!token.isType(TokenType.RBRACE)){
                Token.retractToken();
                while(true){
                    InitVal initVal = new InitVal();
                    initVal.RInitVal();
                    this.addVn(initVal);
                    token = Token.nextToken();
                    this.addToken(token);
                    if(token.isType(TokenType.COMMA)){
                        continue;
                    } else {
                        break;
                    }
                }
                if(!token.isType(TokenType.RBRACE)){
                    Error.error("<frontend.vn.InitVal>");
                    ret = -1;
                }
            } else {
                this.addToken(token);
            }
        }
        return ret;
    }
}