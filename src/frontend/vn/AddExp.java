package frontend.vn;

import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.*;
import utils.Writer;

import java.util.Base64;

public class AddExp extends Vn{

    public AddExp(){
        super("<frontend.vn.AddExp>");
    }
    public int RAddExp(){
        int ret = 0;
        Token token = null;
        while(true){
            MulExp mulExp = new MulExp();
            mulExp.RMulExp();
            this.addVn(mulExp);
            token = Token.nextToken();
            if(token.isType(TokenType.PLUS) || token.isType(TokenType.MINU)){
                this.addToken(token);
            } else {
                Token.retractToken();
                break;
            }
        }
        this.RProcess();
        return ret;
    }
    public int writeVnVt(Writer writer){
        for(int i = 0;i < this.vns.size();i++){
            Vn vn = this.vns.get(i);
            if(vn.IsVt()){
                Token token = vn.getToken();
                writer.addStr(token.getType().getName() + " " + token.getValue() + "\n");
            } else {
                vn.writeVnVt(writer);
                writer.addStr(this.name + "\n");
            }
        }
        return 0;
    }

    @Override
    public int RAnalysis(SymbolTable symbolTable, Symbol symbol) {
        int ret = 0;
        if(symbol.isArray() && this.vns.size() > 1){
            ret = -2;
        } else {
            for(Vn vn:vns){
                if(!vn.isVt){
                    int retmp = vn.RAnalysis(symbolTable,symbol);
                    if(retmp < 0){
                        ret = retmp;
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        int lastindex = -1;
        int ansindex = 0;
        Vn op = null;
        if(!(value instanceof BasicBlock)){
            return 0;
        }
        value = (BasicBlock) value;
        for(Vn vn:vns){
            if(!vn.isVt){
                ansindex = vn.RLLVM(symbolTable,value);
                if(lastindex != -1){
                    int newindex = symbolTable.newReg();
                    addInstruction(value,symbolTable,newindex,op,lastindex,ansindex);
                    symbolTable.addreg();
                    lastindex = newindex;
                } else {
                    lastindex = ansindex;
                }
            } else {
                op = vn;
            }
        }
        return lastindex;
    }

    protected void addInstruction(Value value, SymbolTable symbolTable, int result,
                                  Vn op, int reg1, int reg2){
        if(!(value instanceof BasicBlock)){
            return;
        }
        value = (BasicBlock) value;
        Operator op1 = new Operator(VarType.INT , symbolTable.getRegByIndex(reg1));
        Operator op2 = new Operator(VarType.INT , symbolTable.getRegByIndex(reg2));

        if(op.getToken().getValue().equals("+")){
            ((BasicBlock) value).addInstruction(new InsAdd(Symbol.reg2str(result), VarType.INT, op1, op2));
        } else {
            ((BasicBlock) value).addInstruction(new InsSub(Symbol.reg2str(result), VarType.INT, op1, op2));
        }
    }
}
