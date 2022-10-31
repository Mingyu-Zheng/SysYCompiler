package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolKind;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;

public class Stmt extends Vn{

    public Stmt(){
        super("<frontend.vn.Stmt>");
    }
    public int RStmt(){
        int ret = 0;
        Token token = Token.nextToken();
        int line = token.getLine();
        if(token.isType(TokenType.PRINTFTK)){
            this.addToken(token);
            token = Token.nextToken();
            if(token.isType(TokenType.LPARENT)){
                this.addToken(token);
                token = Token.nextToken();
                if(token.isType(TokenType.STRCON)){
                    this.addToken(token);
                    line = token.getLine();
                    token = Token.nextToken();
                    while(token.isType(TokenType.COMMA)){
                        this.addToken(token);
                        Exp exp = new Exp();
                        exp.RExp();
                        this.addVn(exp);
                        line = exp.getEndLine();
                        token = Token.nextToken();
                    }
                    if(!token.isType(TokenType.RPARENT)){
                        Error.addError(new Error(line, ErrorType.LACK_RPARENT));
                        this.addToken(new Token(line,TokenType.RPARENT,TokenType.RPARENT.getWord()));
                        ret = -1;
                    } else {
                        this.addToken(token);
                        line = token.getLine();
                        token = Token.nextToken();
                    }

                    if(!token.isType(TokenType.SEMICN)){
                        Token.retractToken();
                        Error.addError(new Error(line,ErrorType.LACK_SEMI));
                        this.addToken(new Token(line,TokenType.SEMICN,TokenType.SEMICN.getWord()));
                        ret = -1;
                    } else {
                        this.addToken(token);
                    }
                } else {
                    Error.error("frontend.vn.Stmt");
                    ret = -1;
                }
            } else {
                Error.error("frontend.vn.Stmt");
                ret = -1;
            }
        } else if(token.isType(TokenType.RETURNTK)){
            this.addToken(token);
            line = token.getLine();
            token = Token.nextToken();
            if (!token.isType(TokenType.SEMICN)){
                Token.retractToken();
                Exp exp = new Exp();
                exp.RExp();
                this.addVn(exp);
                line = exp.getEndLine();
                token = Token.nextToken();
            }
            if(token.isType(TokenType.SEMICN)){
                this.addToken(token);
            } else {
                Token.retractToken();
                Error.addError(new Error(line,ErrorType.LACK_SEMI));
                this.addToken(new Token(line,TokenType.SEMICN,TokenType.SEMICN.getWord()));
                ret = -1;
            }
        } else if(token.isType(TokenType.BREAKTTK) || token.isType(TokenType.CONTINUETK)){
            this.addToken(token);
            line = token.getLine();
            token = Token.nextToken();
            if(token.isType(TokenType.SEMICN)){
                this.addToken(token);
            } else {
                Token.retractToken();
                Error.addError(new Error(line, ErrorType.LACK_SEMI));
                this.addToken(new Token(line, TokenType.SEMICN, TokenType.SEMICN.getWord()));
                ret = -1;
            }
        } else if(token.isType(TokenType.WHILETK)){
            this.addToken(token);
            token = Token.nextToken();
            if(token.isType(TokenType.LPARENT)){
                this.addToken(token);
                Cond cond = new Cond();
                cond.RCond();
                this.addVn(cond);
                line = cond.getEndLine();
                token = Token.nextToken();
                if(!token.isType(TokenType.RPARENT)){
                    Token.retractToken();
                    Error.addError(new Error(line,ErrorType.LACK_RPARENT));
                    this.addToken(new Token(line,TokenType.RPARENT,TokenType.RPARENT.getWord()));
                    ret = -1;
                }
                this.addToken(token);
                Stmt stmt = new Stmt();
                stmt.RStmt();
                this.addVn(stmt);

            } else {
                Error.error("<frontend.vn.Stmt>");
                ret = -1;
            }
        } else if(token.isType(TokenType.IFTK)){
            this.addToken(token);
            token = Token.nextToken();
            if(token.isType(TokenType.LPARENT)){
                this.addToken(token);
                Cond cond = new Cond();
                cond.RCond();
                this.addVn(cond);
                line = cond.getEndLine();
                token = Token.nextToken();

                if(!token.isType(TokenType.RPARENT)){
                    Token.retractToken();
                    Error.addError(new Error(line, ErrorType.LACK_RPARENT));
                    this.addToken(new Token(line, TokenType.RPARENT, TokenType.RPARENT.getWord()));
                    ret = -1;
                } else {
                    this.addToken(token);
                }

                Stmt stmt = new Stmt();
                stmt.RStmt();
                this.addVn(stmt);
                token = Token.nextToken();
                if(token.isType(TokenType.ELSETK)){
                    this.addToken(token);
                    stmt = new Stmt();
                    stmt.RStmt();
                    this.addVn(stmt);
                } else {
                    Token.retractToken();
                }
            } else {
                Error.error("<frontend.vn.Stmt>");
                ret = -1;
            }
        } else if(token.isType(TokenType.LBRACE)){
            Token.retractToken();
            Block block = new Block();
            block.RBlock();
            this.addVn(block);
        } else if(token.isType(TokenType.SEMICN)) {
            this.addToken(token);
        } else {
            Token.retractToken();

            boolean isLVal = false;

            int tptr = Token.getTptr();

            Exp exp = new Exp();
            exp.RExp();
            token = Token.nextToken();
            if(token.isType(TokenType.ASSIGN)){
                Token.setTptr(tptr);
                isLVal = true;
            }

            if(isLVal){
                LVal lVal = new LVal();
                lVal.RLVal();
                this.addVn(lVal);
                token = Token.nextToken();
                if(token.isType(TokenType.ASSIGN)){
                    this.addToken(token);
                    token = Token.nextToken();
                    if(token.isType(TokenType.GETINTTK)){
                        this.addToken(token);
                        token = Token.nextToken();

                        if(token.isType(TokenType.LPARENT)){
                            this.addToken(token);
                            line = token.getLine();


                            token = Token.nextToken();
                            if(!token.isType(TokenType.RPARENT)){
                                Error.addError(new Error(line, ErrorType.LACK_RPARENT));
                                this.addToken(new Token(line, TokenType.RPARENT, TokenType.RPARENT.getWord()));
                                ret = -1;
                            } else {
                                this.addToken(token);
                                line = token.getLine();
                                token = Token.nextToken();
                            }

                            if(!token.isType(TokenType.SEMICN)){
                                Token.retractToken();
                                Error.addError(new Error(line, ErrorType.LACK_SEMI));
                                this.addToken(new Token(line, TokenType.SEMICN, TokenType.SEMICN.getWord()));
                                ret = -1;
                            } else {
                                this.addToken(token);
                            }

                        } else {
                            Error.error("<frontend.vn.Stmt>");
                            ret = -1;
                        }
                    } else {
                        Token.retractToken();
                        exp = new Exp();
                        exp.RExp();
                        this.addVn(exp);
                        line = exp.getEndLine();
                        token = Token.nextToken();
                        if(token.isType(TokenType.SEMICN)){
                            this.addToken(token);
                        } else {
                            Token.retractToken();
                            Error.addError(new Error(line, ErrorType.LACK_SEMI));
                            this.addToken(new Token(line, TokenType.SEMICN, TokenType.SEMICN.getWord()));
                            ret = -1;
                        }
                    }
                } else {
                    Error.error("<frontend.vn.Stmt>");
                    ret = -1;
                }
            } else {

                this.addVn(exp);
                line = exp.getEndLine();
                if(token.isType(TokenType.SEMICN)){
                    this.addToken(token);
                } else {
                    Token.retractToken();
                    Error.addError(new Error(line, ErrorType.LACK_SEMI));
                    this.addToken(new Token(line, TokenType.SEMICN, TokenType.SEMICN.getWord()));
                    ret = -1;
                }
            }

        }
        return ret;
    }

    public boolean isFormatString(String str){
        int index = 0;
        while(str.charAt(index) != '\"'){
            index++;
        }
        index++;
        while(str.charAt(index) != '\"'){
            char c = str.charAt(index++);
            if(c == 32 || c == 33 || c == '%' || (c >= 40 && c <= 126)){
                if(c == '%'){
                    c = str.charAt(index++);
                    if(c != 'd'){
                        return false;
                    }
                } else if(c == '\\'){
                    c = str.charAt(index++);
                    if(c != 'n'){
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        if(index < str.length() - 1){
            return false;
        }
        return true;
    }

    public boolean isStringExpNum(String str){
        int len = str.length();
        int cnt1 = 0, cnt2 = 0;
        for (int i = 0;i < len;i++){
            if(str.charAt(i) == '%'){
                cnt1++;
            }
        }
        for(Vn vn:vns){
            if(vn instanceof Exp){
                cnt2++;
            }
        }
        if(cnt1 != cnt2){
            return false;
        }
        return true;
    }

    public int RAnalysis(SymbolTable symbolTable){
        int ret = 0;
        Vn vn0 = vns.get(0);
        if(vn0.isVt){
            if(vn0.getToken().isType(TokenType.RETURNTK)){
                for(Vn vn:vns){
                    if(vn.isVt){
                        continue;
                    }
                    if(vn.RAnalysis(symbolTable) == -1){
                        ret = -1;
                    }
                }
            } else if(vn0.getToken().isType(TokenType.WHILETK)){
                for(Vn vn:vns){
                    if(vn.isVt){
                        continue;
                    }
                    if(vn instanceof Stmt){
                        if(vn.RAnalysis(symbolTable, TokenType.WHILETK.getWord()) == -1){
                            ret = -1;
                        }
                    } else {
                        if(vn.RAnalysis(symbolTable) == -1){
                            ret = -1;
                        }
                    }
                }
            } else if(vn0.getToken().isType(TokenType.IFTK)){
                for(Vn vn:vns){
                    if(vn.isVt){
                        continue;
                    }
                    if(vn.RAnalysis(symbolTable) == -1){
                        ret = -1;
                    }
                }
            } else if(vn0.getToken().isType(TokenType.BREAKTTK)){
                Error.addError(new Error(vn0.getEndLine(), ErrorType.NOLOOP_BREAKORCONTINUE));
            } else if(vn0.getToken().isType(TokenType.CONTINUETK)){
                Error.addError(new Error(vn0.getEndLine(), ErrorType.NOLOOP_BREAKORCONTINUE));
            } else if(vn0.getToken().isType(TokenType.PRINTFTK)){
                Vn vn2 = vns.get(2);
                String formatString = vn2.getToken().getValue();
                if(!isFormatString(formatString)){
                    Error.addError(new Error(vn2.getEndLine(), ErrorType.ILLEGAL_SYMBOL));
                    ret = -1;
                } else if(!isStringExpNum(formatString)){
                    Error.addError(new Error(vn0.getEndLine(), ErrorType.PRINTF_NUMBER));
                    ret = -1;
                }
            }
        } else {
            if(vn0 instanceof Exp){
                ret = vn0.RAnalysis(symbolTable);
            } else if(vn0 instanceof Block){
                ret = vn0.RAnalysis(symbolTable);
            } else if(vn0 instanceof LVal){
                ret = vn0.RAnalysis(symbolTable);
                if(ret == 0){
                    Symbol symbol = symbolTable.getSymbol(vn0.getToken().getValue(), SymbolKind.VAR, SymbolKind.CONST, SymbolKind.PARA);

                    if(symbol.getKind().isSymbolKind(SymbolKind.CONST)){
                        Error.addError(new Error(vn0.getEndLine(), ErrorType.CONST_CHANGE));
                        ret = -1;
                    }
                }
                for(Vn vn:vns){
                    if(vn.isVt || vn instanceof LVal){
                        continue;
                    }
                    if(vn.RAnalysis(symbolTable) == -1){
                        ret = -1;
                    }
                }
            }
        }
        return ret;
    }

    public int RAnalysis(SymbolTable symbolTable, String key){
        int ret = 0;
        Vn vn0 = vns.get(0);
        if(vn0.isVt){
            if(vn0.getToken().isType(TokenType.RETURNTK)){
                ret = this.RAnalysis(symbolTable);
            } else if(vn0.getToken().isType(TokenType.WHILETK)){
                ret = this.RAnalysis(symbolTable);
            } else if(vn0.getToken().isType(TokenType.IFTK)){
                for(Vn vn:vns){
                    if(vn.isVt){
                        continue;
                    }
                    if(vn.RAnalysis(symbolTable, key) == -1){
                        ret = -1;
                    }
                }
            } else if(vn0.getToken().isType(TokenType.BREAKTTK)){
                ;
            } else if(vn0.getToken().isType(TokenType.CONTINUETK)){
                ;
            } else if(vn0.getToken().isType(TokenType.PRINTFTK)){
                ret = this.RAnalysis(symbolTable);
            }
        } else {
            if(vn0 instanceof Exp){
                ret = this.RAnalysis(symbolTable);
            } else if(vn0 instanceof Block){
                ret = vn0.RAnalysis(symbolTable, key);
            } else if(vn0 instanceof LVal){
                ret = this.RAnalysis(symbolTable);
            }
        }
        return ret;
    }
}
