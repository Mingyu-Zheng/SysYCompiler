package frontend.vn;

import error.Error;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.*;

public class PrimaryExp extends Vn{

    public PrimaryExp(){
        super("<frontend.vn.PrimaryExp>");
    }
    public int RPrimaryExp(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.LPARENT)){
            this.addToken(token);
            Exp exp = new Exp();
            exp.RExp();
            this.addVn(exp);
            token = Token.nextToken();
            if(token.isType(TokenType.RPARENT)){
                this.addToken(token);
            } else {
                Error.error("<frontend.vn.PrimaryExp>");
                ret = -1;
            }
        } else if(token.isType(TokenType.IDENFR)){
            Token.retractToken();
            LVal lVal = new LVal();
            lVal.RLVal();
            this.addVn(lVal);
        } else if(token.isType(TokenType.INTCON)){
            Token.retractToken();
            Number number = new Number();
            number.RNumber();
            this.addVn(number);
        } else {
            Error.error("<frontend.vn.PrimaryExp>");
            ret = -1;
        }
        return ret;
    }

    @Override
    public int RAnalysis(SymbolTable symbolTable, Symbol symbol) {
        int ret = 0;
        Vn vn0 = this.vns.get(0);
        if(vn0.isVt){
            if(symbol.isArray()){
                ret = -2;
            } else {
                if(vn0.getToken().isType(TokenType.LPARENT)){
                    for(Vn vn:vns){
                        if(!vn.isVt){
                            int retmp = vn.RAnalysis(symbolTable,symbol);
                            if(retmp < 0){
                                ret = retmp;
                            }
                        }
                    }
                }
            }

        } else {
            ret = vn0.RAnalysis(symbolTable,symbol);
        }
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        Vn vn0 = vns.get(0);
        int retindex = -1;
        if(vn0 instanceof LVal){
            retindex = vn0.RLLVM(symbolTable, value);
            int newindex = symbolTable.newReg();
            Operator operator = new Operator(VarType.INT_POINTER, symbolTable.getRegByIndex(retindex));
            value = (BasicBlock) value;
            ((BasicBlock) value).addInstruction(new InsLoad(symbolTable.getRegByIndex(newindex),VarType.INT,operator));
            retindex = newindex;
        } else if(vn0 instanceof Number){
            int newindex = symbolTable.newReg();
            int number = ((Number) vn0).getInt();
            value = (BasicBlock) value;
            Operator op1 = new Operator(VarType.INT,number);
            Operator op2 = new Operator(VarType.INT, 0);
            ((BasicBlock) value).addInstruction(new InsAdd(symbolTable.getRegByIndex(newindex),VarType.INT,op1,op2));
            return newindex;
        } else {
            retindex = vns.get(1).RLLVM(symbolTable,value);
        }
        return retindex;
    }

    @Override
    public int computeValue(SymbolTable symbolTable) {
        Vn vn0 = vns.get(0);
        if(vn0 instanceof LVal){
            return vn0.computeValue(symbolTable);
        } else if(vn0 instanceof Number){
            return ((Number) vn0).getInt();
        } else {
            Vn vnadd = vns.get(1).vns.get(0);
            return vnadd.computeValue(symbolTable);
        }
    }
}
