package frontend.vn;

import java.util.ArrayList;

import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import midend.llvm.Value;
import utils.Writer;

public class Vn{
    protected String name = "";
    protected Token vt = null;
    protected ArrayList<Vn> vns = null;
    protected boolean isVt = false;

    public Vn(){
        this.vns = new ArrayList<>();
    }

    public Vn(Token vt){
        this.vns = new ArrayList<>();
        this.vt = vt;
        this.isVt = true;
    }

    protected Vn(String name){
        this.vns = new ArrayList<>();
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public Token getToken(){
        if(this.isVt){
            return this.vt;
        }
        return this.vns.get(0).getToken();
    }

    public void getTokens(ArrayList<Token> thetokens){
        for(Vn vn: vns){
            if(vn.isVt){
                thetokens.add(vn.getToken());
            } else {
                vn.getTokens(thetokens);
            }
        }
    }

    public int getEndLine(){
        if(this.isVt){
            return this.vt.getLine();
        } else {
            int end = this.vns.size() - 1;
            return this.vns.get(end).getEndLine();
        }
    }

    public boolean IsVt(){
        return this.isVt;
    }

    public boolean isClassOf(String name){
        if(this.name.equals(name)){
            return true;
        }
        return false;
    }

    public void addVn(Vn vn){
        this.vns.add(vn);
    }

    public void addToken(Token token){
        Vn vn = new Vn(token);
        this.addVn(vn);
    }



    public int RProcess(){

        return 0;
    }

    public int RAnalysis(SymbolTable symbolTable){
        int ret = 0;
        for(Vn vn:vns){
            if(vn.isVt){
                continue;
            } else {
                if(vn.RAnalysis(symbolTable) == -1){
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public int RAnalysis(SymbolTable symbolTable, String key){
        int ret = 0;
        for(Vn vn:vns){
            if(vn.isVt){
                continue;
            } else {
                if(vn.RAnalysis(symbolTable) == -1){
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public int RAnalysis(SymbolTable symbolTable, Symbol symbol){
        int ret = 0;
        for(Vn vn:vns){
            if(vn.isVt){
                continue;
            } else {
                if(vn.RAnalysis(symbolTable) == -1){
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public int RLLVM(SymbolTable symbolTable, Value value){
        int ret = 0;
        for(Vn vn:vns){
            if(!vn.isVt && !(vn instanceof Btype)){
                ret = vn.RLLVM(symbolTable, value);
            }
        }
        return ret;
    }

    public int RLLVM(SymbolTable symbolTable, Value value, int isglobal) {
        for(Vn vn:vns){
            if(!vn.isVt && !(vn instanceof Btype)){
                vn.RLLVM(symbolTable, value, isglobal);
            }
        }
        return 0;
    }

    public int computeValue(SymbolTable symbolTable){
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
        writer.addStr(this.name + "\n");
        return 0;
    }
}


