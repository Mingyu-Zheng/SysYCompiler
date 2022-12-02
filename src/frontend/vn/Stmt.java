package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolKind;
import frontend.symbol.SymbolTable;
import frontend.symbol.SymbolType;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.*;

import java.util.ArrayList;

public class Stmt extends Vn{

    public Stmt(){
        super("<Stmt>");
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

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        int ret = 0;
        Vn vn0 = vns.get(0);
        if(vn0.isVt){
            if(vn0.getToken().isType(TokenType.RETURNTK)){
                Vn vn1 = vns.get(1);
                if(vn1 instanceof Exp){
                    ret = vn1.RLLVM(symbolTable, value);
                    value = (BasicBlock) value;
                    ((BasicBlock) value).addInstruction(new InsRet(VarType.INT, symbolTable.getRegByIndex(ret)));
                } else {
                    value = (BasicBlock) value;
                    ((BasicBlock) value).addInstruction(new InsRet(VarType.VOID, ""));
                }
            } else if(vn0.getToken().isType(TokenType.WHILETK)){
                Vn vnor = vns.get(2).vns.get(0);
                ValueFuncDef fatherFunc = ((BasicBlock) value).getFatherFunc();
                BasicBlock blockloop = new BasicBlock(fatherFunc);
                BasicBlock blocktrue = new BasicBlock(fatherFunc);
                BasicBlock blockend = new BasicBlock(fatherFunc);
                BasicBlock blockoutloop = new BasicBlock(fatherFunc);
                blocktrue.setContinueBlock(blockend);
                blocktrue.setBreakBlock(blockoutloop);
                blockend.addInstruction(new Jump(blockloop));
                BasicBlock blockto = blockoutloop;

                ((BasicBlock) value).addNextBlock(blockloop);

                BasicBlock blockjudge = null;

                for(Vn vnand:vnor.vns){
                    if(vnand.isVt){
                        continue;
                    }
                    BasicBlock blockorend = null;
                    for(Vn vneq:vnand.vns){
                        if(vneq.isVt){
                            continue;
                        }
                        BasicBlock blockeq = new BasicBlock();
                        blockloop.addNextBlock(blockeq);
                        if(vneq.vns.size() > 1){
                            Vn vneq1 = vneq.vns.get(1);
                            int retleft = vneq.vns.get(0).RLLVM(symbolTable, blockeq);
                            int retright = ((EqExp) vneq).RLLVM(symbolTable, blockeq, true, 2);
                            Operator opl = new Operator(VarType.INT, symbolTable.getRegByIndex(retleft));
                            Operator opr = new Operator(VarType.INT, symbolTable.getRegByIndex(retright));
                            if(vneq1.getToken().getValue().equals("==")){
                                blockeq.addInstruction(new BranchBne(opl, opr, blockto));
                            } else {
                                blockeq.addInstruction(new BranchBeq(opl, opr, blockto));
                            }
                        } else {
                            Vn vnrel = vneq.vns.get(0);
                            if(vnrel.vns.size() > 1){
                                Vn vnrel1 = vnrel.vns.get(1);
                                int retleft = vnrel.vns.get(0).RLLVM(symbolTable, blockeq);
                                int retright = ((RelExp) vnrel).RLLVM(symbolTable, blockeq, true, 2);
                                Operator opl = new Operator(VarType.INT, symbolTable.getRegByIndex(retleft));
                                Operator opr = new Operator(VarType.INT, symbolTable.getRegByIndex(retright));
                                if(vnrel1.getToken().getValue().equals(">")){
                                    blockeq.addInstruction(new BranchBlez(opl, opr, blockto));
                                } else if(vnrel1.getToken().getValue().equals("<")) {
                                    blockeq.addInstruction(new BranchBgez(opl, opr, blockto));
                                } else if(vnrel1.getToken().getValue().equals(">=")) {
                                    blockeq.addInstruction(new BranchBltz(opl, opr, blockto));
                                } else {
                                    blockeq.addInstruction(new BranchBgtz(opl, opr, blockto));
                                }
                            } else {
                                int retindex = vnrel.vns.get(0).RLLVM(symbolTable, blockeq);
                                Operator opl = new Operator(VarType.INT, symbolTable.getRegByIndex(retindex));
                                Operator opr = new Operator(VarType.INT, 0);
                                blockeq.addInstruction(new BranchBeq(opl, opr, blockto));
                            }
                        }
                        blockorend = blockeq;
                    }
                    blockjudge = blockorend;
                }
                Vn vn4 = vns.get(4);
                if(vn4.vns.get(0) instanceof Block){
                    SymbolTable newsymboltable = symbolTable.newSonBlockTable();
                    vn4.RLLVM(newsymboltable, blocktrue);
                    symbolTable.setRegIndexAndIndexWithSon(newsymboltable);
                } else {
                    vn4.RLLVM(symbolTable, blocktrue);
                }
                blockloop.addNextBlock(blocktrue);
                blockloop.addNextBlock(blockend);
                blockloop.addNextBlock(blockoutloop);
                ret = 0;
            } else if(vn0.getToken().isType(TokenType.IFTK)){
                Vn vnor = vns.get(2).vns.get(0);
                ValueFuncDef fatherFunc = ((BasicBlock) value).getFatherFunc();
                BasicBlock blocktrue = new BasicBlock(fatherFunc);
                BasicBlock blockfalse = new BasicBlock(fatherFunc);
                // BasicBlock blockfalsetoend = new BasicBlock(fatherFunc);
                BasicBlock blockend = new BasicBlock(fatherFunc);
                blocktrue.setContinueBlock(blockend);
                blockfalse.setContinueBlock(blockend);

                boolean isjumptrue = true;
                BasicBlock blockto = blockfalse;
                if(vns.size() < 6){
                    blockto = blockend;
                    isjumptrue = false;
                }

                BasicBlock blockjudge = null;
                for(Vn vnand:vnor.vns){
                    if(vnand.isVt){
                        continue;
                    }
                    BasicBlock blockorend = null;
                    for(Vn vneq:vnand.vns){
                        if(vneq.isVt){
                            continue;
                        }
                        BasicBlock blockeq = new BasicBlock();
                        ((BasicBlock) value).addNextBlock(blockeq);
                        if(vneq.vns.size() > 1){
                            Vn vneq1 = vneq.vns.get(1);
                            int retleft = vneq.vns.get(0).RLLVM(symbolTable, blockeq);
                            int retright = ((EqExp) vneq).RLLVM(symbolTable, blockeq, true, 2);
                            Operator opl = new Operator(VarType.INT, symbolTable.getRegByIndex(retleft));
                            Operator opr = new Operator(VarType.INT, symbolTable.getRegByIndex(retright));
                            if(vneq1.getToken().getValue().equals("==")){
                                blockeq.addInstruction(new BranchBne(opl, opr, blockto));
                            } else {
                                blockeq.addInstruction(new BranchBeq(opl, opr, blockto));
                            }
                        } else {
                            Vn vnrel = vneq.vns.get(0);
                            if(vnrel.vns.size() > 1){
                                Vn vnrel1 = vnrel.vns.get(1);
                                int retleft = vnrel.vns.get(0).RLLVM(symbolTable, blockeq);
                                int retright = ((RelExp) vnrel).RLLVM(symbolTable, blockeq, true, 2);
                                Operator opl = new Operator(VarType.INT, symbolTable.getRegByIndex(retleft));
                                Operator opr = new Operator(VarType.INT, symbolTable.getRegByIndex(retright));
                                if(vnrel1.getToken().getValue().equals(">")){
                                    blockeq.addInstruction(new BranchBlez(opl, opr, blockto));
                                } else if(vnrel1.getToken().getValue().equals("<")) {
                                    blockeq.addInstruction(new BranchBgez(opl, opr, blockto));
                                } else if(vnrel1.getToken().getValue().equals(">=")) {
                                    blockeq.addInstruction(new BranchBltz(opl, opr, blockto));
                                } else {
                                    blockeq.addInstruction(new BranchBgtz(opl, opr, blockto));
                                }
                            } else {
                                int retindex = vnrel.vns.get(0).RLLVM(symbolTable, blockeq);
                                Operator opl = new Operator(VarType.INT, symbolTable.getRegByIndex(retindex));
                                Operator opr = new Operator(VarType.INT, 0);
                                blockeq.addInstruction(new BranchBeq(opl, opr, blockto));
                            }
                        }
                        blockorend = blockeq;
                    }
                    if(isjumptrue){
                        blockorend.addInstruction(new Jump(blocktrue));
                    }
                    blockjudge = blockorend;
                }
                if(vns.size() > 6){
                    blockjudge.addNextBlock(blockfalse);
                    Vn vn6 = vns.get(6);
                    if(vn6.vns.get(0) instanceof Block){
                        SymbolTable newsymboltable = symbolTable.newSonBlockTable();
                        vn6.RLLVM(newsymboltable, blockfalse);
                        symbolTable.setRegIndexAndIndexWithSon(newsymboltable);
                    } else {
                        vn6.RLLVM(symbolTable, blockfalse);
                    }
                    blockfalse = (BasicBlock) blockfalse.getEndBlock();
                    blockfalse.addInstruction(new Jump(blockend));
                }
                Vn vn4 = vns.get(4);
                if(vn4.vns.get(0) instanceof Block){
                    SymbolTable newsymboltable = symbolTable.newSonBlockTable();
                    vn4.RLLVM(newsymboltable, blocktrue);
                    symbolTable.setRegIndexAndIndexWithSon(newsymboltable);
                } else {
                    vn4.RLLVM(symbolTable, blocktrue);
                }
                ((BasicBlock) value).addNextBlock(blocktrue);
                ((BasicBlock) value).addNextBlock(blockend);
                ret = 0;
            } else if(vn0.getToken().isType(TokenType.BREAKTTK)){
                ((BasicBlock) value).addInstruction(new Jump(((BasicBlock) value).getBreakBlock()));
                ret = 0;
            } else if(vn0.getToken().isType(TokenType.CONTINUETK)){
                ((BasicBlock) value).addInstruction(new Jump(((BasicBlock) value).getContinueBlock()));
                ret = 0;
            } else if(vn0.getToken().isType(TokenType.PRINTFTK)){
                Vn vn2 = vns.get(2);
                String formatString = vn2.getToken().getValue();
                ArrayList<Integer> rets = new ArrayList<>();
                for(Vn vn:vns){
                    if(vn instanceof Exp){
                        int newindex = vn.RLLVM(symbolTable, value);
                        rets.add(newindex);
                    }
                }
                this.LLVMprint(symbolTable,value,formatString,rets);
            }
        } else {
            if(vn0 instanceof LVal){
                Vn vn2 = vns.get(2);
                ret = vn0.RLLVM(symbolTable,value);
                if(vn2 instanceof Exp){
                    int outindex = vns.get(2).RLLVM(symbolTable, value);
                    Operator op1 = new Operator(VarType.INT, symbolTable.getRegByIndex(outindex));
                    Operator op2 = new Operator(VarType.INT_POINTER, symbolTable.getRegByIndex(ret));
                    value = (BasicBlock) value;
                    ((BasicBlock) value).addInstruction(new InsStore(VarType.INT, op1, op2));
                } else {
                    int newindex = symbolTable.newReg();
                    value = (BasicBlock) value;
                    ((BasicBlock) value).addInstruction(new InsCall(symbolTable.getRegByIndex(newindex),
                            VarType.INT, "0getint"));
                    Operator op1 = new Operator(VarType.INT, symbolTable.getRegByIndex(newindex));
                    Operator op2 = new Operator(VarType.INT_POINTER, symbolTable.getRegByIndex(ret));
                    ((BasicBlock) value).addInstruction(new InsStore(VarType.INT, op1, op2));
                }
            } else if(vn0 instanceof Exp){
                ret = vn0.RLLVM(symbolTable,value);
            } else if(vn0 instanceof Block){
                SymbolTable newsymbolTable = symbolTable.newSonBlockTable();
                ret = vn0.RLLVM(newsymbolTable, value);
                symbolTable.setRegIndexAndIndexWithSon(newsymbolTable);
            }
        }
        return ret;
    }

    public int LLVMprint(SymbolTable symbolTable, Value value, String str, ArrayList<Integer> regs){
        int index = 0;
        int reg = -1;
        int i = 1;
        int j = 1;
        value = (BasicBlock) value;
        while(i < str.length() - 1){
            char ch = str.charAt(i);
            if(ch == '%' && i + 1 < str.length() - 1 && str.charAt(i+1) == 'd'){
                if(j < i){
                    String strContent = "\"" + str.substring(j, i) + "\"";
                    int strNum = symbolTable.getStrnum();
                    String symname = "0s" + strNum;
                    Symbol symbol = new Symbol(symname, SymbolKind.CONST);
                    symbol.setStrContent(strContent);
                    symbol.setType(SymbolType.STR);
                    symbolTable.addSymbol2Root(symbol);
                    symbolTable.addStrnum();
                    Operator operator = new Operator(VarType.STR, "@" + symname);
                    ((BasicBlock) value).addInstruction(new InsCall(VarType.VOID, "0putstr",operator));
                }
                Operator operator = new Operator(VarType.INT, symbolTable.getRegByIndex(regs.get(index)));
                index++;
                ((BasicBlock) value).addInstruction(new InsCall(VarType.VOID, "0putint",operator));
                i = i + 2;
                j = i;
            } else if(i + 1 == str.length() - 1){
                if(j < i){
                    String strContent = "\"" + str.substring(j, i + 1) + "\"";
                    int strNum = symbolTable.getStrnum();
                    String symname = "0s" + strNum;
                    Symbol symbol = new Symbol(symname, SymbolKind.CONST);
                    symbol.setStrContent(strContent);
                    symbol.setType(SymbolType.STR);
                    symbolTable.addSymbol2Root(symbol);
                    symbolTable.addStrnum();
                    Operator operator = new Operator(VarType.STR, "@" + symname);
                    ((BasicBlock) value).addInstruction(new InsCall(VarType.VOID, "0putstr", operator));
                }
                i++;
                j = i;
            } else {
                i++;
            }
        }
        return 0;
    }
}
