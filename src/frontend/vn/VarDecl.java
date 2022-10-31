package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.token.Token;
import frontend.token.TokenType;

public class VarDecl extends Vn{

    public VarDecl(){
        super("<frontend.vn.VarDecl>");
    }
    public int RVarDecl(){
        int ret = 0;
        Btype btype = new Btype();
        btype.RBtype();
        this.addVn(btype);
        while(true){
            VarDef varDef = new VarDef();
            varDef.RVarDef();
            this.addVn(varDef);
            Token token = Token.nextToken();
            if(token.isType(TokenType.COMMA)){
                this.addToken(token);
                continue;
            } else if(token.isType(TokenType.SEMICN)){
                this.addToken(token);
                break;
            } else {
                Token.retractToken();
                Error.addError(new Error(varDef.getEndLine(), ErrorType.LACK_SEMI));
                this.addToken(new Token(varDef.getEndLine(),TokenType.SEMICN,TokenType.SEMICN.getWord()));
                ret = -1;
                break;
            }
        }
        return ret;
    }
}
