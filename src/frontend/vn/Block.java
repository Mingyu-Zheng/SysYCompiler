package frontend.vn;

import error.Error;
import error.ErrorType;
import frontend.symbol.SymbolFuncType;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.BasicBlock;
import midend.llvm.Value;

public class Block extends Vn{

    public Block(){
        super("<Block>");
    }
    public int RBlock(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.LBRACE)){
            this.addToken(token);
            token = Token.nextToken();
            while(!token.isType(TokenType.RBRACE)){
                Token.retractToken();
                BlockItem blockItem = new BlockItem();
                blockItem.RBlockItem();
                this.addVn(blockItem);
                token = Token.nextToken();
                if(token == null){
                    break;
                }
            }
            this.addToken(token);
        } else {
            Error.error("<Block>");
            ret = -1;
        }
        return ret;
    }

    public int RAnalysis(SymbolTable symbolTable, String key){
        int ret = 0;
        if(TokenType.isType(key, TokenType.WHILETK)){
            SymbolTable newSymbolTable = symbolTable.newSonFuncTable();
            for(Vn vn:vns){
                if(!vn.isVt){
                    if(vn.RAnalysis(newSymbolTable, key) == -1){
                        ret = -1;
                    }
                }
            }
        } else {
            boolean iserror = true;
            if(SymbolFuncType.isFuncType(key,SymbolFuncType.INT)){
                for(Vn vn:vns){
                    if(vn.isVt){
                        continue;
                    }
                    Vn vnson = vn.vns.get(0);
                    if(vnson instanceof Stmt){
                        Vn vnsonson = vnson.vns.get(0);
                        if(vnsonson.isVt){
                            if(vnsonson.getToken().isType(TokenType.RETURNTK)){
                                iserror = false;
                                break;
                            }
                        }
                    }
                }
                if(iserror){
                    Error.addError(new Error(this.getEndLine(), ErrorType.FUNCNOVOID_NORETURN));
                    ret = -1;
                }
            } else {
                iserror = false;
                int line = 0;
                for(Vn vn:vns){
                    if(vn.isVt){
                        continue;
                    }
                    Vn vnson = vn.vns.get(0);
                    if(vnson instanceof Stmt){
                        Vn vnsonson = vnson.vns.get(0);
                        if(vnsonson.isVt){
                            if(vnsonson.getToken().isType(TokenType.RETURNTK)){
                                if(vnson.vns.get(1) instanceof Exp){
                                    iserror = true;
                                    line = vnsonson.getEndLine();
                                    break;
                                }
                            }
                        }
                    }
                }
                if(iserror){
                    Error.addError(new Error(line,ErrorType.FUNCVOID_RETURN));
                    ret = -1;
                }
            }
            for(Vn vn:vns){
                if(!vn.isVt){
                    if(vn.RAnalysis(symbolTable) == -1){
                        ret = -1;
                    }
                }
            }
        }

        return 0;
    }

    public int RAnalysis(SymbolTable symbolTable){
        int ret = 0;
        SymbolTable newSymbolTable = symbolTable.newSonFuncTable();
        for(Vn vn:vns){
            if(!vn.isVt){
                if(vn.RAnalysis(newSymbolTable) == -1){
                    ret = -1;
                }
            }
        }
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        int ret = 0;
        for(Vn vn:vns){
            if(!vn.isVt && !(vn instanceof Btype)){
                ret = vn.RLLVM(symbolTable, value);
                value = ((BasicBlock) value).getEndBlock();
            }
        }
        return ret;
    }

}
