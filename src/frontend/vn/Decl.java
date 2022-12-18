package frontend.vn;

import error.Error;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.Value;
import utils.Writer;

public class Decl extends Vn{

    public Decl(){
        super("<Decl>");
    }
    public int RDecl(){
        Token token = Token.nextToken();
        if(token.isType(TokenType.CONSTTK)){
            Token.retractToken();
            ConstDecl constDecl = new ConstDecl();
            constDecl.RConstDecl();
            this.addVn(constDecl);
        }
        else if(token.isType(TokenType.INTTK)){
            Token.retractToken();
            VarDecl varDecl = new VarDecl();
            varDecl.RVarDecl();
            this.addVn(varDecl);
        }
        else {
            Token.retractToken();
            Error.error("<Decl>");
        }
        return 0;
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
}