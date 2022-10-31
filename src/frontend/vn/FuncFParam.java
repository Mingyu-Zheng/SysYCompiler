package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolKind;
import frontend.symbol.SymbolTable;
import frontend.symbol.SymbolType;
import frontend.token.Token;
import frontend.token.TokenType;

public class FuncFParam extends Vn{

    public FuncFParam(){
        super("<frontend.vn.FuncFParam>");
    }
    public int RFuncFParam(){
        int ret = 0;
        Token token = null;
        Btype btype = new Btype();
        btype.RBtype();
        this.addVn(btype);
        token = Token.nextToken();
        if(token.isType(TokenType.IDENFR)){
            this.addToken(token);
            token = Token.nextToken();
            if(token.isType(TokenType.LBRACK)){
                this.addToken(token);
                int line = token.getLine();
                token = Token.nextToken();

                if(!token.isType(TokenType.RBRACK)){
                    Token.retractToken();
                    Error.addError(new Error(line, ErrorType.LACK_RBRACK));
                    this.addToken(new Token(line,TokenType.RBRACK,TokenType.RBRACK.getWord()));
                    ret = -1;
                } else {
                    this.addToken(token);
                    token = Token.nextToken();
                }

                while(token.isType(TokenType.LBRACK)){
                    this.addToken(token);
                    ConstExp constExp = new ConstExp();
                    constExp.RConstExp();
                    this.addVn(constExp);
                    token = Token.nextToken();
                    if(!token.isType(TokenType.RBRACK)){
                        Error.addError(new Error(constExp.getEndLine(),ErrorType.LACK_RBRACK));
                        this.addToken(new Token(constExp.getEndLine(),TokenType.RBRACK,TokenType.RBRACK.getWord()));
                        ret = -1;
                    } else {
                        this.addToken(token);
                        token = Token.nextToken();
                    }
                }
                Token.retractToken();

            } else {
                Token.retractToken();
            }
        } else {
            Error.error("<frontend.vn.FuncFParam>");
            ret = -1;
        }
        return ret;
    }

    public int RAnalysis(SymbolTable symbolTable){
        int ret = 0;
        int dim = 0;
        String name = this.vns.get(1).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.PARA);
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
        if(symbolTable.isSymbolExistThis(name, SymbolKind.PARA)){
            Error.addError(new Error(vns.get(0).getEndLine(),ErrorType.NAME_REDEFINE));
            ret = -1;
        } else {
            symbolTable.addSymbol(symbol);
        }
        return ret;
    }
}
