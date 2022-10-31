package frontend.vn;

import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import utils.Writer;

public class MulExp extends Vn{

    public MulExp(){
        super("<frontend.vn.MulExp>");
    }
    public int RMulExp(){
        int ret = 0;
        Token token = null;
        while(true){
            UnaryExp unaryExp = new UnaryExp();
            unaryExp.RUnaryExp();
            this.addVn(unaryExp);
            token = Token.nextToken();
            if(token.isType(TokenType.MULT) || token.isType(TokenType.DIV) || token.isType(TokenType.MOD)){
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

    @Override
    public int RAnalysis(SymbolTable symbolTable, Symbol symbol) {
        int ret = 0;
        if(symbol.isArray() && this.vns.size() > 1){
            ret = -2;
        } else {
            for(Vn vn:vns){
                if(!vn.isVt){
                    int retmp = vn.RAnalysis(symbolTable,symbol);
                    if(retmp < 0){
                        ret = retmp;
                    }
                }
            }
        }
        return ret;
    }
}
