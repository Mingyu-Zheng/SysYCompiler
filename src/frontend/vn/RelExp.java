package frontend.vn;

import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.Value;
import utils.Writer;

public class RelExp extends Vn{

    public RelExp(){
        super("<RelExp>");
    }
    public int RRelExp(){
        int ret = 0;
        Token token = null;
        while(true){
            AddExp addExp = new AddExp();
            addExp.RAddExp();
            this.addVn(addExp);
            token = Token.nextToken();
            if(token.isType(TokenType.LSS) || token.isType(TokenType.GRE) ||
                    token.isType(TokenType.LEQ) || token.isType(TokenType.GEQ)){
                this.addToken(token);
            } else {
                Token.retractToken();
                break;
            }
        }
        this.RProcess();
        return ret;
    }
    public int RLLVM(SymbolTable symbolTable, Value value, boolean isCond, int index) {
        int ret = 0;

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
