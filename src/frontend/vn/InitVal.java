package frontend.vn;

import error.Error;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.Value;

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

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        int ret = -1;
        Vn vn0 = vns.get(0);
        if(vn0 instanceof Exp){
            ret = vn0.RLLVM(symbolTable, value);
        } else {

        }
        return ret;
    }
}