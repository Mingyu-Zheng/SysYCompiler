package frontend.vn;

import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.*;
import utils.Writer;

public class EqExp extends Vn {

    public EqExp(){
        super("<EqExp>");
    }
    public int REqExp(){
        int ret = 0;
        Token token = null;
        while(true){
            RelExp relExp = new RelExp();
            relExp.RRelExp();
            this.addVn(relExp);
            token = Token.nextToken();
            if(token.isType(TokenType.EQL) || token.isType(TokenType.NEQ)){
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
    public int RAnalysis(SymbolTable symbolTable, Symbol symbol){
        int ret = 0;
        Vn vn0 = this.vns.get(0);
        ret = vn0.RAnalysis(symbolTable, symbol);
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        return this.RLLVM(symbolTable, value, true, 0);
    }

    public int RLLVM(SymbolTable symbolTable, Value value, boolean isCond, int index) {
        int lastindex = -1;
        int ansindex = 0;
        Vn op = null;
        for(int i = index; i < vns.size();i++){
            Vn vn = vns.get(i);
            if(!vn.isVt){
                ansindex = vn.RLLVM(symbolTable,value);
                if(ansindex != -1){
                    if(lastindex != -1){
                        int newindex = symbolTable.newReg();
                        addInstruction(value,symbolTable,newindex,op,lastindex,ansindex);
                        lastindex = newindex;
                    } else {
                        lastindex = ansindex;
                    }
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

        if(op.getToken().getValue().equals("==")){
            ((BasicBlock) value).addInstruction(new InsSeq(symbolTable.getRegByIndex(result), VarType.INT, op1, op2));
        } else {
            ((BasicBlock) value).addInstruction(new InsSne(symbolTable.getRegByIndex(result), VarType.INT, op2, op1));
        }
    }
}
