package frontend.vn;

import error.Error;
import frontend.token.Token;
import frontend.token.TokenType;
import utils.Writer;

public class Btype extends Vn{

    public Btype(){
        super("<frontend.vn.Btype>");
    }
    public int RBtype(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.INTTK)){
            this.addToken(token);
        } else {
            Error.error("<frontend.vn.Btype>");
            ret = -1;
        }
        return ret;
    }
    public int writeVnVt(Writer writer){
        for(Vn vn:this.vns){
            if(vn.IsVt()){
                Token token = vn.getToken();
                writer.addStr(token.getType().getName() + " " + token.getValue() + "\n");
            } else {
                vn.writeVnVt(writer);
            }
        }
        return 0;
    }
}
