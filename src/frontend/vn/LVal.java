package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolKind;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LVal extends Vn{

    public LVal(){
        super("<LVal>");
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
        Vn vn0 = vns.get(0);
        String name = vn0.getToken().getValue();
        Symbol symbol = symbolTable.getSymbol(name, SymbolKind.CONST, SymbolKind.VAR, SymbolKind.PARA);
        if(vns.size() == 1){
            if(symbol == null){
                return -1;
            }
            retindex = symbol.getIndex();
            if(symbol.isArray()){
                if(symbol.isGlobal()){
                    Operator operator = new Operator(VarType.ARRAY, symbolTable.getRegByIndex(retindex));
                    retindex = symbolTable.newMemReg();
                    ((BasicBlock) value).addInstruction(new InsLa(symbolTable.getRegByIndex(retindex), VarType.INT_POINTER,operator));
                }
            }
        } else if(vns.size() <= 4){
            ArrayList<Integer> dimarr = symbol.getDimarray();
            retindex = symbol.getIndex();
            int retmul = -1;
            int retadd = -1;
            int retans = -1;
            if(symbol.isGlobal()){
                Operator operator = new Operator(VarType.ARRAY, symbolTable.getRegByIndex(retindex));
                retindex = symbolTable.newReg();
                ((BasicBlock) value).addInstruction(new InsLa(symbolTable.getRegByIndex(retindex), VarType.INT_POINTER,operator));
            }
            int rettmp = -1;
            for(Vn vn:vns){
                if(vn instanceof Exp){
                    rettmp = vn.RLLVM(symbolTable, value);
                }
            }
            int sz = 4;
            if(dimarr.size() > 1){
                sz = sz * dimarr.get(1);
                retmul = symbolTable.newReg();
                Operator op1 = new Operator(VarType.INT , symbolTable.getRegByIndex(rettmp));
                Operator op2 = new Operator(VarType.INT , sz);
                ((BasicBlock) value).addInstruction(new InsMul(symbolTable.getRegByIndex(retmul), VarType.INT, op1, op2));
                retans = symbolTable.newMemReg();
                Operator op3 = new Operator(VarType.INT_POINTER, symbolTable.getRegByIndex(retindex));
                Operator op4 = new Operator(VarType.INT, symbolTable.getRegByIndex(retmul));
                ((BasicBlock) value).addInstruction(new InsAdd(symbolTable.getRegByIndex(retans), VarType.INT_POINTER, op3, op4));
                retindex = retans;
            } else {
                retmul = symbolTable.newReg();
                Operator op1 = new Operator(VarType.INT , symbolTable.getRegByIndex(rettmp));
                Operator op2 = new Operator(VarType.INT , sz);
                ((BasicBlock) value).addInstruction(new InsMul(symbolTable.getRegByIndex(retmul), VarType.INT, op1, op2));
                retadd = symbolTable.newMemReg();
                Operator op3 = new Operator(VarType.INT_POINTER, symbolTable.getRegByIndex(retindex));
                Operator op4 = new Operator(VarType.INT, symbolTable.getRegByIndex(retmul));
                ((BasicBlock) value).addInstruction(new InsAdd(symbolTable.getRegByIndex(retadd), VarType.INT_POINTER, op3, op4));
                retindex = retadd;
            }
        } else {
            ArrayList<Integer> dimarr = symbol.getDimarray();
            retindex = symbol.getIndex();
            if(symbol.isGlobal()){
                Operator operator = new Operator(VarType.ARRAY, symbolTable.getRegByIndex(retindex));
                retindex = symbolTable.newReg();
                ((BasicBlock) value).addInstruction(new InsLa(symbolTable.getRegByIndex(retindex), VarType.INT_POINTER,operator));
            }
            ArrayList<Integer> retarr = new ArrayList<>();
            for(Vn vn:vns){
                if(vn instanceof Exp){
                    int rettmp = vn.RLLVM(symbolTable, value);
                    retarr.add(rettmp);
                }
            }
            int retmul1 = symbolTable.newReg();
            Operator op1 = new Operator(VarType.INT , symbolTable.getRegByIndex(retarr.get(0)));
            Operator op2 = new Operator(VarType.INT , dimarr.get(1));
            ((BasicBlock) value).addInstruction(new InsMul(symbolTable.getRegByIndex(retmul1), VarType.INT, op1, op2));
            int retadd1 = symbolTable.newReg();
            Operator op3 = new Operator(VarType.INT, symbolTable.getRegByIndex(retmul1));
            Operator op4 = new Operator(VarType.INT, symbolTable.getRegByIndex(retarr.get(1)));
            ((BasicBlock) value).addInstruction(new InsAdd(symbolTable.getRegByIndex(retadd1), VarType.INT, op3, op4));
            int retmul2 = symbolTable.newReg();
            Operator op5 = new Operator(VarType.INT, symbolTable.getRegByIndex(retadd1));
            Operator op6 = new Operator(VarType.INT, 4);
            ((BasicBlock) value).addInstruction(new InsMul(symbolTable.getRegByIndex(retmul2), VarType.INT, op5, op6));
            int retadd2 = symbolTable.newMemReg();
            Operator op7 = new Operator(VarType.INT_POINTER, symbolTable.getRegByIndex(retindex));
            Operator op8 = new Operator(VarType.INT, symbolTable.getRegByIndex(retmul2));
            ((BasicBlock) value).addInstruction(new InsAdd(symbolTable.getRegByIndex(retadd2),VarType.INT_POINTER,op7, op8));
            retindex = retadd2;
        }
        return retindex;
    }

    @Override
    public int computeValue(SymbolTable symbolTable) {
        int ret = 0;
        String name = vns.get(0).getToken().getValue();
        Symbol symbol = symbolTable.getSymbol(name, SymbolKind.CONST, SymbolKind.VAR);
        if(vns.size() == 1){
            if(symbol == null){
                ret = -1;
            } else {
                ret = symbol.getConstvalue();
            }
        } else {
            if(symbol == null){
                ret = -1;
            } else {
                ArrayList<Integer> posarr = new ArrayList<>();
                for(Vn vn:vns){
                    if (vn instanceof Exp){
                        posarr.add(vn.computeValue(symbolTable));
                    }
                }
                int sz = 1;
                if(symbol.getDimarray().size() > 1){
                    sz = symbol.getDimarray().get(1);
                }
                int[] array = symbol.getArrayValue();
                if(posarr.size() == 1){
                    ret = array[posarr.get(0)];
                } else if(posarr.size() > 1){
                    ret = array[posarr.get(0) * sz + posarr.get(1)];
                }
            }
        }
        return ret;
    }
}
