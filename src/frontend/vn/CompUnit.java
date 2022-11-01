package frontend.vn;

import error.Error;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.Value;

public class CompUnit extends Vn{

    public CompUnit(){
        super("<frontend.vn.CompUnit>");
    }
    public int RCompUnit(){
        int ret = 0;
        Token token1 = Token.nextToken();
        Token token2 = Token.nextToken();
        Token token3 = Token.nextToken();
        while(!token3.isType(TokenType.LPARENT)){
            Token.retractToken();
            Token.retractToken();
            Token.retractToken();
            Decl decl = new Decl();
            decl.RDecl();
            this.addVn(decl);
            token1 = Token.nextToken();
            token2 = Token.nextToken();
            token3 = Token.nextToken();
        }
        Token.retractToken();
        while(!token2.isType(TokenType.MAINTK)){
            Token.retractToken();
            Token.retractToken();
            FuncDef funcDef = new FuncDef();
            funcDef.RFuncDef();
            this.addVn(funcDef);
            token1 = Token.nextToken();
            token2 = Token.nextToken();
        }
        MainFuncDef mainFuncDef = new MainFuncDef();
        if(!token1.isType(TokenType.INTTK)){
            Error.error();
            ret = -1;
        }
        Token.retractToken();
        Token.retractToken();
        mainFuncDef.RMainFuncDef();
        this.addVn(mainFuncDef);
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        for(Vn vn:vns){
            if(vn instanceof Decl){
                vn.RLLVM(symbolTable,value,1);
            } else {
                vn.RLLVM(symbolTable,value);
            }
        }
        return 0;
    }
}
