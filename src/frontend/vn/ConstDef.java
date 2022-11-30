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

    protected int valueProcess(int dim, Symbol symbol, Vn constInitVal, SymbolTable symbolTable){
        int constInitValue = 0;
        if(dim == 0){
            symbol.setType(SymbolType.INT);
            if(constInitVal != null){
                constInitValue = constInitVal.vns.get(0).computeValue(symbolTable);
            }
            symbol.setConstvalue(constInitValue);
        }
        return constInitValue;
    }

    protected int[] valueProcess(int dim, Symbol symbol, Vn constInitVal,
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
            if(constInitVal != null){
                if(dim == 1){
                    int i = 0;
                    for(Vn vn:constInitVal.vns){
                        if(vn instanceof ConstInitVal){
                            array[i] = vn.vns.get(0).computeValue(symbolTable);
                            i++;
                        }
                    }
                } else {
                    int i = 0, j = 0;
                    int sz = dimarr.get(1);
                    for (Vn vn:constInitVal.vns){
                        if(vn instanceof ConstInitVal){
                            for(Vn vn1:vn.vns){
                                if(vn1 instanceof ConstInitVal){
                                    array[i * sz + j] = vn1.vns.get(0).computeValue(symbolTable);
                                    j++;
                                }
                            }
                            i++;
                            j = 0;
                        }
                    }
                }
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
    public int RLLVM(SymbolTable symbolTable, Value value, int isglobal) {
        int ret = 0;
        int dim = 0;
        int[] array = null;
        ArrayList<Integer> dimarr = new ArrayList<>();

        String name = this.vns.get(0).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.CONST);
        Vn constInitVal = null;
        int constInitValue = 0;

        dim = dimProcess(dimarr, symbolTable);
        for(Vn vn:this.vns){
            if(vn instanceof ConstInitVal){
                constInitVal = vn;
            }
        }

        value = (ValueModule) value;
        if(dim == 0){
            constInitValue = valueProcess(dim, symbol, constInitVal, symbolTable);
            symbolTable.addSymbol2Global(symbol);
            ((ValueModule) value).addGlobalDecl(new ValueGlobalDef(name, VarType.INT, constInitValue));
        } else {
            array = valueProcess(dim, symbol, constInitVal, symbolTable, dimarr);
            symbolTable.addSymbol2Global(symbol);
            ((ValueModule) value).addGlobalDecl(new ValueGlobalDef(name, VarType.ARRAY, array));
        }
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        int ret = 0;
        int dim = 0;
        int[] array = null;
        ArrayList<Integer> dimarr = new ArrayList<>();

        String name = this.vns.get(0).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.CONST);
        Vn constInitVal = null;
        int constInitValue = 0;

        dim = dimProcess(dimarr, symbolTable);
        for(Vn vn:this.vns){
            if(vn instanceof ConstInitVal){
                constInitVal = vn;
            }
        }

        if(dim == 0){
            constInitValue = valueProcess(dim, symbol, constInitVal, symbolTable);
            symbolTable.addSymbolMem2Reg(symbol);
            ret = symbol.getIndex();
            int number = constInitValue;
            ((BasicBlock) value).addInstruction(new InsAlloc(symbolTable.getRegByIndex(ret),VarType.INT));
            Operator op1 = new Operator(VarType.INT,number);
            Operator op2 = new Operator(VarType.INT_POINTER,symbolTable.getRegByIndex(ret));
            ((BasicBlock) value).addInstruction(new InsStore(VarType.INT,op1,op2));
        } else {
            array = valueProcess(dim, symbol, constInitVal, symbolTable, dimarr);
            symbolTable.addSymbolMem2Reg(symbol);
            ret = symbol.getIndex();
            int memSize = 4;
            for(Integer i:dimarr){
                memSize = memSize * i;
            }
            ((BasicBlock) value).addInstruction(new InsAlloc(symbolTable.getRegByIndex(ret),VarType.INT, memSize));
            for(int i = 0;i < array.length;i++){
                int number = array[i];
                Operator op1 = new Operator(VarType.INT,number);
                Operator op2 = new Operator(VarType.INT_POINTER,symbolTable.getRegByIndex(ret));
                ((BasicBlock) value).addInstruction(new InsStore(VarType.INT,op1,op2,4 * i));
            }
        }
        return ret;
    }
}
