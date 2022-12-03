package frontend.vn;

import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.*;
import utils.Writer;

public class RelExp extends Vn{

    public RelExp(){
        super("<RelExp>");
    }
    public int RRelExp(){
        int ret = 0;
        Token token = null;
        while(true){
            AddExp addExp = new AddExp();
            addExp.RAddExp();
            this.addVn(addExp);
            token = Token.nextToken();
            if(token.isType(TokenType.LSS) || token.isType(TokenType.GRE) ||
                    token.isType(TokenType.LEQ) || token.isType(TokenType.GEQ)){
                this.addToken(token);
            } else {
                Token.retractToken();
                break;
            }
        }
        this.RProcess();
        return ret;
    }

    @Override
    public int RLLVM(SymbolTable symbolTable, Value value) {
        return this.RLLVM(symbolTable,value,true,this.vns.size() - 1);
    }

    public int RLLVM(SymbolTable symbolTable, Value value, boolean isCond, int index) {
        int lastindex = -1;
        int ansindex = 0;
        Vn op = null;
        for(int i = 0; i <= index ;i++){
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

        if(op.getToken().getValue().equals("<")){
            ((BasicBlock) value).addInstruction(new InsSlt(symbolTable.getRegByIndex(result), VarType.INT, op1, op2));
        } else if(op.getToken().getValue().equals(">")) {
            ((BasicBlock) value).addInstruction(new InsSlt(symbolTable.getRegByIndex(result), VarType.INT, op2, op1));
        } else if(op.getToken().getValue().equals("<=")) {
            ((BasicBlock) value).addInstruction(new InsSle(symbolTable.getRegByIndex(result), VarType.INT, op1, op2));
        } else {
            ((BasicBlock) value).addInstruction(new InsSle(symbolTable.getRegByIndex(result), VarType.INT, op2, op1));
        }
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
}
