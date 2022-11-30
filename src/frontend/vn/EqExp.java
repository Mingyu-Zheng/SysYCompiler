package frontend.vn;

import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.Value;
import utils.Writer;

public class EqExp extends Vn {

    public EqExp(){
        super("<frontend.vn.EqExp>");
    }
    public int REqExp(){
        int ret = 0;
        Token token = null;
        while(true){
            RelExp relExp = new RelExp();
            relExp.RRelExp();
            this.addVn(relExp);
            token = Token.nextToken();
            if(token.isType(TokenType.EQL) || token.isType(TokenType.NEQ)){
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
    public int RAnalysis(SymbolTable symbolTable, Symbol symbol){
        int ret = 0;
        Vn vn0 = this.vns.get(0);
        ret = vn0.RAnalysis(symbolTable, symbol);
        return ret;
    }

    public int RLLVM(SymbolTable symbolTable, Value value, boolean isCond, int index) {
        int ret = 0;

        return ret;
    }
}
