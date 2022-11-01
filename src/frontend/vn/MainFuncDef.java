package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.symbol.SymbolFuncType;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.*;

public class MainFuncDef extends Vn{

    public MainFuncDef(){
        super("<frontend.vn.MainFuncDef>");
    }
    public int RMainFuncDef(){
        int ret = 0;
        Token token1 = Token.nextToken();
        Token token2 = Token.nextToken();
        Token token3 = Token.nextToken();
        Token token4 = Token.nextToken();
        if(token1.isType(TokenType.INTTK) && token2.isType(TokenType.MAINTK) &&
                token3.isType(TokenType.LPARENT)){
            this.addToken(token1);
            this.addToken(token2);
            this.addToken(token3);
        }

        if (token4.isType(TokenType.RPARENT)){
            this.addToken(token4);
        } else {
            Token.retractToken();
            Error.addError(new Error(token3.getLine(), ErrorType.LACK_RPARENT));
            this.addToken(new Token(token3.getLine(),TokenType.RPARENT,TokenType.RPARENT.getWord()));
            ret = -1;
        }
        Block block = new Block();
        block.RBlock();
        this.addVn(block);
        return ret;
    }

    public int RAnalysis(SymbolTable symbolTable){
        int ret = 0;
        SymbolTable newSymbolTable = symbolTable.newSonTable();
        for(Vn vn:vns){
            if(vn instanceof Block){
                if(vn.RAnalysis(newSymbolTable, SymbolFuncType.INT.getName())==-1){
                    ret = -1;
                }
            } else if(!vn.isVt){
                if(vn.RAnalysis(newSymbolTable) == -1){
                    ret = -1;
                }
            }
        }
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        SymbolTable newSymbolTable = symbolTable.newSonTable();
        value = (ValueMudule) value;
        ValueFuncDef funcDef = new ValueFuncDef(VarType.INT, vns.get(1).getToken().getValue());
        ((ValueMudule) value).addFuncDef(funcDef);
        BasicBlock basicBlock = new BasicBlock();
        funcDef.addBasicBlock(basicBlock);

        newSymbolTable.setFuncRegNum();

        Vn vnblock = vns.get(4);
        vnblock.RLLVM(newSymbolTable, basicBlock);
        return 0;
    }
}
