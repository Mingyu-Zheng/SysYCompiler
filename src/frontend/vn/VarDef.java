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

public class VarDef extends Vn{

    public VarDef(){
        super("<VarDef>");
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
        for(Vn vninit:vns){
            if(!vninit.isVt){
                int renew = vninit.RAnalysis(symbolTable);
                if(renew == -1){
                    ret = -1;
                }
            }
        }
        return ret;
    }

    protected int dimProcess(ArrayList<Integer> dimarr, SymbolTable symbolTable){
        int dim = 0;
        for(Vn vn:this.vns){
            if(vn.isVt){
                if(vn.getToken().isType(TokenType.LBRACK)){
                    dim++;
                }
            } else {
                if(vn instanceof ConstExp){
                    int size = ((ConstExp) vn).computeValue(symbolTable);
                    dimarr.add(size);
                }
            }
        }
        return dim;
    }

    protected int valueProcess(int dim, Symbol symbol, Vn initVal, SymbolTable symbolTable){
        int constInitValue = 0;
        if(dim == 0){
            symbol.setType(SymbolType.INT);
            if(initVal != null){
                constInitValue = initVal.vns.get(0).computeValue(symbolTable);
            }
            symbol.setConstvalue(constInitValue);
            symbol.setInit(true);
        }
        return constInitValue;
    }

    protected int[] valueProcess(int dim, Symbol symbol, Vn initVal,
                                 SymbolTable symbolTable, ArrayList<Integer> dimarr){
        int[] array = null;
        if(dim != 0) {
            symbol.setType(SymbolType.ARRAY);
            symbol.setArrayDim(dim);
            symbol.setDimarray(dimarr);
            int size = 1;
            for(Integer i:dimarr){
                size = size * i;
            }
            array = new int[size];
            if(initVal != null){
                if(dim == 1){
                    int i = 0;
                    for(Vn vn:initVal.vns){
                        if(vn instanceof InitVal){
                            array[i] = vn.vns.get(0).computeValue(symbolTable);
                            i++;
                        }
                    }
                } else {
                    int i = 0, j = 0;
                    int sz = dimarr.get(1);
                    for (Vn vn:initVal.vns){
                        if(vn instanceof InitVal){
                            for(Vn vn1:vn.vns){
                                if(vn1 instanceof InitVal){
                                    array[i * sz + j] = vn1.vns.get(0).computeValue(symbolTable);
                                    j++;
                                }
                            }
                            i++;
                            j = 0;
                        }
                    }
                }
                symbol.setInit(true);
            } else {
                for(int i = 0;i < array.length;i++){
                    array[i] = 0;
                }
            }
            symbol.setArrayValue(array);
        }
        return array;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        int ret = 0;
        int newindex = -1;
        int dim = 0;
        int[] array = null;
        ArrayList<Integer> dimarr = new ArrayList<>();

        String name = this.vns.get(0).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.VAR);
        Vn initVal = null;

        dim = dimProcess(dimarr, symbolTable);
        for(Vn vn:this.vns){
            if(vn instanceof InitVal){
                initVal = vn;
            }
        }

        symbolTable.addSymbolMem2Reg(symbol);
        ret = symbol.getIndex();
        int memSize = 4;
        for(Integer i:dimarr){
            memSize = memSize * i;
        }

        if(dim == 0){
            symbol.setType(SymbolType.INT);
            ((BasicBlock) value).addInstruction(new InsAlloc(symbolTable.getRegByIndex(ret),VarType.INT));
            if(initVal != null){
                newindex = initVal.RLLVM(symbolTable,value);
                Operator op1 = new Operator(VarType.INT, symbolTable.getRegByIndex(newindex));
                Operator op2 = new Operator(VarType.INT_POINTER,symbolTable.getRegByIndex(ret));
                ((BasicBlock) value).addInstruction(new InsStore(VarType.INT,op1,op2));
            }
        } else if (dim == 1){
            symbol.setType(SymbolType.ARRAY);
            symbol.setArrayDim(dim);
            symbol.setDimarray(dimarr);
            ((BasicBlock) value).addInstruction(new InsAlloc(symbolTable.getRegByIndex(ret),VarType.ARRAY, memSize));
            if(initVal != null){
                int i = 0;
                for(Vn vn:initVal.vns){
                    if(vn instanceof InitVal){
                        newindex = vn.RLLVM(symbolTable,value);
                        Operator op1 = new Operator(VarType.INT, symbolTable.getRegByIndex(newindex));
                        Operator op2 = new Operator(VarType.INT_POINTER,symbolTable.getRegByIndex(ret));
                        ((BasicBlock) value).addInstruction(new InsStore(VarType.INT, op1, op2, i * 4));
                        i++;
                    }
                }
            }
        } else {
            symbol.setType(SymbolType.ARRAY);
            symbol.setArrayDim(dim);
            symbol.setDimarray(dimarr);
            ((BasicBlock) value).addInstruction(new InsAlloc(symbolTable.getRegByIndex(ret),VarType.ARRAY, memSize));
            int sz = dimarr.get(1);
            if(initVal != null){
                int i = 0;
                for(Vn vn:initVal.vns){
                    if(vn instanceof InitVal){
                        int j = 0;
                        for(Vn vn1:vn.vns){
                            if(vn1 instanceof InitVal){
                                newindex = vn1.RLLVM(symbolTable,value);
                                Operator op1 = new Operator(VarType.INT, symbolTable.getRegByIndex(newindex));
                                Operator op2 = new Operator(VarType.INT_POINTER,symbolTable.getRegByIndex(ret));
                                ((BasicBlock) value).addInstruction(new InsStore(VarType.INT, op1, op2, (i * sz + j) * 4));
                                j++;
                            }
                        }
                        i++;
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value, int isglobal) {
        int ret = 0;
        int dim = 0;
        int[] array = null;
        ArrayList<Integer> dimarr = new ArrayList<>();

        String name = this.vns.get(0).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.VAR);
        Vn initVal = null;
        int constInitValue = 0;

        dim = dimProcess(dimarr, symbolTable);
        for(Vn vn:this.vns){
            if(vn instanceof InitVal){
                initVal = vn;
            }
        }

        value = (ValueModule) value;
        if(dim == 0){
            constInitValue = valueProcess(dim, symbol, initVal, symbolTable);
            symbolTable.addSymbol2Global(symbol);
            ((ValueModule) value).addGlobalDecl(new ValueGlobalDef(name, VarType.INT, constInitValue));

        } else {
            array = valueProcess(dim, symbol, initVal, symbolTable, dimarr);
            symbolTable.addSymbol2Global(symbol);
            ((ValueModule) value).addGlobalDecl(new ValueGlobalDef(name, VarType.ARRAY, symbol.isInit(), array));
        }
        return ret;
    }
}
