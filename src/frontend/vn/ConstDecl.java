package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.token.Token;
import frontend.token.TokenType;

public class ConstDecl extends Vn{

    public ConstDecl(){
        super("<frontend.vn.ConstDecl>");
    }
    public int RConstDecl(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.CONSTTK)){
            this.addToken(token);
            Btype btype = new Btype();
            btype.RBtype();
            this.addVn(btype);
            ConstDef constDef = null;
            while(true){
                constDef = new ConstDef();
                constDef.RConstDef();
                this.addVn(constDef);
                token = Token.nextToken();

                if(token.isType(TokenType.SEMICN)){
                    this.addToken(token);
                    break;
                } else if(token.isType(TokenType.COMMA)){
                    this.addToken(token);
                    continue;
                } else {
                    Token.retractToken();
                    Error.addError(new Error(constDef.getEndLine(), ErrorType.LACK_SEMI));
                    this.addToken(new Token(constDef.getEndLine(),TokenType.SEMICN,TokenType.SEMICN.getWord()));
                    ret = -1;
                    break;
                }
            }
        } else {
            Error.error("<frontend.vn.ConstDecl>");
            ret = -1;
        }
        return ret;
    }
}
