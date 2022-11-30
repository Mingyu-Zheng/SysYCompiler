package frontend.vn;

import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.BasicBlock;
import midend.llvm.Value;
import utils.Writer;

public class BlockItem extends Vn{

    public BlockItem(){
        super("<frontend.vn.BlockItem>");
    }
    public int RBlockItem(){
        int ret = 0;
        Token token = Token.nextToken();
        if(token.isType(TokenType.CONSTTK) || token.isType(TokenType.INTTK)){
            Token.retractToken();
            Decl decl = new Decl();
            decl.RDecl();
            this.addVn(decl);
        } else {
            Token.retractToken();
            Stmt stmt = new Stmt();
            stmt.RStmt();
            this.addVn(stmt);
        }
        return ret;
    }
    public int writeVnVt(Writer writer){
        for(Vn vn:this.vns){
            if(vn.IsVt()){
                Token token = vn.getToken();
                writer.addStr(token.getType().getName() + " " + token.getValue() + "\n");
            } else {
                vn.writeVnVt(writer);
            }
        }
        return 0;
    }
    public int RAnalysis(SymbolTable symbolTable, String key){
        int ret = 0;
        for(Vn vn:vns){
            if(vn.isVt){
                continue;
            } else {
                if(vn instanceof Stmt){
                    if(vn.RAnalysis(symbolTable, key) == -1){
                        ret = -1;
                    }
                } else {
                    if(vn.RAnalysis(symbolTable) == -1){
                        ret = -1;
                    }
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
            }
        }
        return ret;
    }
}
