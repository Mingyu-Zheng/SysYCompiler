package frontend.vn;

import frontend.token.Token;
import frontend.token.TokenType;
import utils.Writer;

public class LAndExp extends Vn{

    public LAndExp(){
        super("<LAndExp>");
    }
    public int RLAndExp(){
        int ret = 0;
        Token token = null;
        while(true){
            EqExp eqExp = new EqExp();
            eqExp.REqExp();
            this.addVn(eqExp);
            token = Token.nextToken();
            if(token.isType(TokenType.AND)){
                this.addToken(token);
            } else {
                Token.retractToken();
                break;
            }
        }
        this.RProcess();
        return ret;
    }
    public int writeVnVt(Writer writer){
        for(int i = 0;i < this.vns.size();i++){
            Vn vn = this.vns.get(i);
            if(vn.IsVt()){
                Token token = vn.getToken();
                writer.addStr(token.getType().getName() + " " + token.getValue() + "\n");
            } else {
                vn.writeVnVt(writer);
                writer.addStr(this.name + "\n");
            }
        }
        return 0;
    }
}
