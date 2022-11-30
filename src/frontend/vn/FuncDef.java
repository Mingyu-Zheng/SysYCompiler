package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.symbol.*;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.*;

public class FuncDef extends Vn{

    public FuncDef(){
        super("<frontend.vn.FuncDef>");
    }
    public int RFuncDef(){
        int ret = 0;
        FuncType funcType = new FuncType();
        funcType.RFuncType();
        this.addVn(funcType);
        Token token = Token.nextToken();
        this.addToken(token);
        if(token.isType(TokenType.IDENFR)){
            token = Token.nextToken();
            this.addToken(token);
            if(token.isType(TokenType.LPARENT)){
                int line = token.getLine();
                token = Token.nextToken();
                if((!token.isType(TokenType.RPARENT)) && (!token.isType(TokenType.LBRACE))){
                    Token.retractToken();
                    FuncFParams funcFParams = new FuncFParams();
                    funcFParams.RFuncFParams();
                    this.addVn(funcFParams);
                    line = funcFParams.getEndLine();
                    token = Token.nextToken();
                }
                if(!token.isType(TokenType.RPARENT)){
                    Token.retractToken();
                    Error.addError(new Error(line, ErrorType.LACK_RPARENT));
                    this.addToken(new Token(line,TokenType.RPARENT,TokenType.RPARENT.getWord()));
                    ret = -1;
                } else {
                    this.addToken(token);
                }
                Block block = new Block();
                block.RBlock();
                this.addVn(block);

            } else {
                Error.error("<frontend.vn.FuncDef>");
                ret = -1;
            }
        } else {
            Error.error("<frontend.vn.FuncDef>");
            ret = -1;
        }
        return ret;
    }

    public int RAnalysis(SymbolTable symbolTable){
        int ret = 0;
        int num = 0;
        String name = this.vns.get(1).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.FUNC, SymbolType.INT);
        String functype = this.vns.get(0).getToken().getValue();
        if(SymbolFuncType.isFuncType(functype, SymbolFuncType.INT)){
            symbol.setFuncType(SymbolFuncType.INT);
        } else {
            symbol.setFuncType(SymbolFuncType.VOID);
        }

        if(this.vns.get(3) instanceof FuncFParams){
            for(Vn vn:this.vns.get(3).vns){
                if(vn instanceof FuncFParam){
                    num++;
                }
            }
        }
        symbol.setFuncFParamNum(num);

        if(symbolTable.isSymbolExistThis(name, SymbolKind.FUNC)){
            Error.addError(new Error(vns.get(0).getEndLine(),ErrorType.NAME_REDEFINE));
            ret = -1;
        } else {
            symbolTable.addSymbol(symbol);
        }

        SymbolTable newSymbolTable = symbolTable.newSonFuncTable();
        if(this.vns.get(3) instanceof FuncFParams){
            for(Vn vn:vns.get(3).vns){
                if(!vn.isVt) {
                    if (vn.RAnalysis(newSymbolTable) == -1) {
                        ret = -1;
                    }
                }
            }
        }

        newSymbolTable.addSymbolPara(symbol);
        Vn vnend = vns.get(vns.size() - 1);
        if(vnend.RAnalysis(newSymbolTable, symbol.getFuncType().getName()) == -1){
            ret = -1;
        }
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        int ret = 0;
        int num = 0;
        String name = this.vns.get(1).getToken().getValue();
        Symbol symbol = new Symbol(name, SymbolKind.FUNC, SymbolType.INT);
        String functype = this.vns.get(0).getToken().getValue();
        if(SymbolFuncType.isFuncType(functype, SymbolFuncType.INT)){
            symbol.setFuncType(SymbolFuncType.INT);
        } else {
            symbol.setFuncType(SymbolFuncType.VOID);
        }

        if(this.vns.get(3) instanceof FuncFParams){
            for(Vn vn:this.vns.get(3).vns){
                if(vn instanceof FuncFParam){
                    num++;
                }
            }
        }
        symbol.setFuncFParamNum(num);
        symbolTable.addSymbol2Global(symbol);

        SymbolTable newSymbolTable = symbolTable.newSonFuncTable();
        value = (ValueMudule) value;
        ValueFuncDef funcDef = null;
        if(symbol.getFuncType().isFuncType(SymbolFuncType.INT)){
            funcDef = new ValueFuncDef(VarType.INT, vns.get(1).getToken().getValue());
        } else {
            funcDef = new ValueFuncDef(VarType.VOID, vns.get(1).getToken().getValue());
        }

        ((ValueMudule) value).addFuncDef(funcDef);

        BasicBlock basicBlock = new BasicBlock();
        if(this.vns.get(3) instanceof FuncFParams){
            vns.get(3).RLLVM(newSymbolTable, funcDef.getArgument());
            vns.get(3).RLLVM(newSymbolTable, basicBlock);
        }

        newSymbolTable.addSymbolPara(symbol);
        Vn vnend = vns.get(vns.size() - 1);

        funcDef.addBasicBlock(basicBlock);
        vnend.RLLVM(newSymbolTable, basicBlock);

        funcDef.checkIsVoidRet();

        return ret;
    }
}
