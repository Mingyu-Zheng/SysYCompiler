package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolKind;
import frontend.symbol.SymbolTable;
import frontend.symbol.SymbolType;
import frontend.token.Token;
import frontend.token.TokenType;

public class VarDef extends Vn{

    public VarDef(){
        super("<frontend.vn.VarDef>");
    }
    public int RVarDef(){
        int ret = 0;
        Token token = Token.nextToken();
        this.addToken(token);
        if(token.isType(TokenType.IDENFR)){
            token = Token.nextToken();
            while(token.isType(TokenType.LBRACK)){
                this.addToken(token);
                ConstExp constExp = new ConstExp();
                constExp.RConstExp();
                this.addVn(constExp);
                token = Token.nextToken();

                if(!token.isType(TokenType.RBRACK)){
                    Error.addError(new Error(constExp.getEndLine(), ErrorType.LACK_RBRACK));
                    this.addToken(new Token(constExp.getEndLine(),TokenType.RBRACK,TokenType.RBRACK.getWord()));
                    ret = -1;
                } else {
                    this.addToken(token);
                    token = Token.nextToken();
                }

            }
            if(token.isType(TokenType.ASSIGN)){
                this.addToken(token);
                InitVal initVal = new InitVal();
                initVal.RInitVal();
                this.addVn(initVal);
            } else {
                Token.retractToken();
            }
        } else {
            Error.error();
            ret = -1;
        }
        return ret;
    }
    public int RAnalysis(SymbolTable symbolTable){
        int ret = 0;
        int dim = 0;
        String name = this.vns.get(0).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.VAR);
        for(Vn vn:this.vns){
            if(vn.isVt){
                if(vn.getToken().isType(TokenType.LBRACK)){
                    dim++;
                }
            }
        }
        if(dim == 0){
            symbol.setType(SymbolType.INT);
        } else {
            symbol.setType(SymbolType.ARRAY);
            symbol.setArrayDim(dim);
        }
        if(symbolTable.isSymbolExistThis(name, SymbolKind.VAR)){
            Error.addError(new Error(vns.get(0).getEndLine(),ErrorType.NAME_REDEFINE));
            ret = -1;
        } else {
            symbolTable.addSymbol(symbol);
        }
        return ret;
    }
}
