package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolKind;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.Value;

public class LVal extends Vn{

    public LVal(){
        super("<frontend.vn.LVal>");
    }
    public int RLVal(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.IDENFR)){
            this.addToken(token);
            token = Token.nextToken();
            while(token.isType(TokenType.LBRACK)){
                this.addToken(token);
                Exp exp = new Exp();
                exp.RExp();
                this.addVn(exp);
                token = Token.nextToken();
                if(token.isType(TokenType.RBRACK)){
                    this.addToken(token);
                    token = Token.nextToken();
                } else {
                    Error.addError(new Error(exp.getEndLine(), ErrorType.LACK_RBRACK));
                    this.addToken(new Token(exp.getEndLine(), TokenType.RBRACK, TokenType.RBRACK.getWord()));
                    ret = -1;
                }
            }
            Token.retractToken();
        } else {
            Error.error("<frontend.vn.LVal>");
            ret = -1;
        }
        return ret;
    }

    public int RAnalysis(SymbolTable symbolTable){
        int ret = 0;
        Vn vn0 = this.vns.get(0);
        Symbol symbol = symbolTable.getSymbol(vn0.getToken().getValue(), SymbolKind.VAR, SymbolKind.CONST, SymbolKind.PARA);
        if(symbol == null){
            Error.addError(new Error(vn0.getEndLine(), ErrorType.NAME_UNDEFINE));
            ret = -1;
        }
        for(Vn vn:vns){
            if(!vn.isVt){
                if(vn.RAnalysis(symbolTable) == -1){
                    ret = -1;
                }
            }
        }
        return ret;
    }

    @Override
    public int RAnalysis(SymbolTable symbolTable, Symbol symbol) {
        int ret = this.RAnalysis(symbolTable);
        if(ret == 0){
            Vn vn0 = this.vns.get(0);
            Symbol s = symbolTable.getSymbol(vn0.getToken().getValue(), SymbolKind.VAR, SymbolKind.CONST, SymbolKind.PARA);
            if(symbol.isArray()){
                if(!s.isArray()){
                    ret = -2;
                }
            }
            int dim = s.getArrayDim();
            for(Vn vn:vns){
                if(vn.isVt){
                    if(vn.getToken().isType(TokenType.LBRACK)){
                        dim--;
                    }
                }
            }
            if(dim != symbol.getArrayDim()){
                ret = -2;
            }

            for(Vn vn:vns){
                if(!vn.isVt){
                    int retmp = vn.RAnalysis(symbolTable);
                    if(retmp < 0){
                        ret = retmp;
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        int retindex = -1;
        if(vns.size() == 1){
            Vn vn0 = vns.get(0);
            String name = vn0.getToken().getValue();
            Symbol symbol = symbolTable.getSymbol(name, SymbolKind.CONST, SymbolKind.VAR, SymbolKind.PARA);
            if(symbol == null){
                return -1;
            }
            retindex = symbol.getIndex();
        } else if(vns.size() <= 4){

        } else {

        }
        return retindex;
    }

    @Override
    public int computeValue(SymbolTable symbolTable) {
        int ret = 0;
        if(vns.size() == 1){
            String name = vns.get(0).getToken().getValue();
            Symbol symbol = symbolTable.getSymbol(name, SymbolKind.CONST);
            if(symbol == null){
                ret = -1;
            } else {
                ret = symbol.getConstvalue();
            }
        } else {

        }
        return ret;
    }
}
