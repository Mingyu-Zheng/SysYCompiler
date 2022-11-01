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

public class ConstDef extends Vn{

    public ConstDef(){
        super("<frontend.vn.ConstDef>");
    }
    public int RConstDef(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.IDENFR)){
            this.addToken(token);
            token = Token.nextToken();
            if(token.isType(TokenType.LBRACK)){
                while(token.isType(TokenType.LBRACK)){
                    this.addToken(token);
                    ConstExp constExp = new ConstExp();
                    constExp.RConstExp();
                    this.addVn(constExp);
                    token = Token.nextToken();

                    if(!token.isType(TokenType.RBRACK)){
                        Error.addError(new Error(token.getLine(), ErrorType.LACK_RBRACK));
                        this.addToken(new Token(token.getLine(),TokenType.RBRACK,TokenType.RBRACK.getWord()));
                        ret = -1;
                    } else {
                        this.addToken(token);
                        token = Token.nextToken();
                    }

                }
            }
            if(token.isType(TokenType.ASSIGN)){
                this.addToken(token);
                ConstInitVal constInitVal = new ConstInitVal();
                constInitVal.RConstInitVal();
                this.addVn(constInitVal);
            } else {
                Error.error("<frontend.vn.ConstDef>");
                ret = -1;
            }
        } else {
            Error.error("<frontend.vn.ConstDef>");
            ret = -1;
        }
        return ret;
    }

    public int RAnalysis(SymbolTable symbolTable){
        int ret = 0;
        int dim = 0;
        String name = this.vns.get(0).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.CONST);
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
        if(symbolTable.isSymbolExistThis(name, SymbolKind.CONST)){
            Error.addError(new Error(vns.get(0).getEndLine(),ErrorType.NAME_REDEFINE));
            ret = -1;
        } else {
            symbolTable.addSymbol(symbol);
        }
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value, int isglobal) {
        int ret = 0;
        int dim = 0;
        String name = this.vns.get(0).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.CONST);
        Vn constInitVal = null;
        int constInitValue = 0;

        for(Vn vn:this.vns){
            if(vn.isVt){
                if(vn.getToken().isType(TokenType.LBRACK)){
                    dim++;
                }
            } else {
                if(vn instanceof ConstInitVal){
                    constInitVal = vn;
                }
            }
        }
        if(dim == 0){
            symbol.setType(SymbolType.INT);
            if(constInitVal != null){
                constInitValue = constInitVal.vns.get(0).computeValue(symbolTable);
            }
        } else {
            symbol.setType(SymbolType.ARRAY);
            symbol.setArrayDim(dim);
        }

        symbol.setConstvalue(constInitValue);
        symbolTable.addSymbol2Global(symbol);

        value = (ValueMudule) value;
        ((ValueMudule) value).addGlobalDecl(new ValueGlobalDecl(name, VarType.INT, constInitValue));

        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        int ret = 0;
        int dim = 0;
        String name = this.vns.get(0).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.CONST);
        Vn constInitVal = null;
        int constInitValue = 0;

        for(Vn vn:this.vns){
            if(vn.isVt){
                if(vn.getToken().isType(TokenType.LBRACK)){
                    dim++;
                }
            } else {
                if(vn instanceof ConstInitVal){
                    constInitVal = vn;
                }
            }
        }
        if(dim == 0){
            symbol.setType(SymbolType.INT);
            if(constInitVal != null){
                constInitValue = constInitVal.vns.get(0).computeValue(symbolTable);
            }
        } else {
            symbol.setType(SymbolType.ARRAY);
            symbol.setArrayDim(dim);
        }

        symbol.setConstvalue(constInitValue);
        symbolTable.addSymbolMem2Reg(symbol);


        ret = symbol.getIndex();
        int number = constInitValue;
        value = (BasicBlock) value;
        ((BasicBlock) value).addInstruction(new InsAlloc(symbolTable.getRegByIndex(ret),VarType.INT));
        Operator op1 = new Operator(VarType.INT,number);
        Operator op2 = new Operator(VarType.INT_POINTER,symbolTable.getRegByIndex(ret));
        ((BasicBlock) value).addInstruction(new InsStore(VarType.INT,op1,op2));
        return ret;
    }
}
