package frontend.vn;

import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.Value;

public class FuncFParams extends Vn{

    public FuncFParams(){
        super("<frontend.vn.FuncFParams>");
    }
    public int RFuncFParams(){
        int ret = 0;
        Token token = null;
        while(true){
            FuncFParam funcFParam = new FuncFParam();
            funcFParam.RFuncFParam();
            this.addVn(funcFParam);
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

}
