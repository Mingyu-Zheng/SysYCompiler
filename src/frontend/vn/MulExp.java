package frontend.vn;

import frontend.symbol.Symbol;
import frontend.symbol.SymbolTable;
import frontend.token.Token;
import frontend.token.TokenType;
import midend.llvm.*;
import utils.Writer;

public class MulExp extends Vn{

    public MulExp(){
        super("<frontend.vn.MulExp>");
    }
    public int RMulExp(){
        int ret = 0;
        Token token = null;
        while(true){
            UnaryExp unaryExp = new UnaryExp();
            unaryExp.RUnaryExp();
            this.addVn(unaryExp);
            token = Token.nextToken();
            if(token.isType(TokenType.MULT) || token.isType(TokenType.DIV) || token.isType(TokenType.MOD)){
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
        int lastreg = -1;
        int ansreg = 0;
        Vn op = null;
        if(!(value instanceof BasicBlock)){
            return 0;
        }
        value = (BasicBlock) value;
        for(Vn vn:vns){
            if(!vn.isVt){
                ansreg = vn.RLLVM(symbolTable,value);
                if(ansreg != -1){
                    if(lastreg != -1){
                        int newreg = symbolTable.newReg();
                        addInstruction(value,symbolTable,newreg,op,lastreg,ansreg);
                        lastreg = newreg;
                    } else {
                        lastreg = ansreg;
                    }
                }
            } else {
                op = vn;
            }
        }
        return lastreg;
    }

    protected void addInstruction(Value value, SymbolTable symbolTable, int result,
                                  Vn op, int reg1, int reg2){
        if(!(value instanceof BasicBlock)){
            return;
        }
        value = (BasicBlock) value;
        Operator op1 = new Operator(VarType.INT , symbolTable.getRegByIndex(reg1));
        Operator op2 = new Operator(VarType.INT , symbolTable.getRegByIndex(reg2));

        if(op.getToken().getValue().equals("*")){
            ((BasicBlock) value).addInstruction(new InsMul(symbolTable.getRegByIndex(result), VarType.INT, op1, op2));
        } else if(op.getToken().getValue().equals("/")) {
            ((BasicBlock) value).addInstruction(new InsSdiv(symbolTable.getRegByIndex(result), VarType.INT, op1, op2));
        } else {
            ((BasicBlock) value).addInstruction(new InsSrem(symbolTable.getRegByIndex(result), VarType.INT, op1, op2));
        }
    }

    public int computeValue(SymbolTable symbolTable){
        int lastvalue = 0;
        int ans = 0;
        Vn op = null;

        for(Vn vn:vns){
            if(!vn.isVt){
                ans = vn.computeValue(symbolTable);
                if(op == null){
                    lastvalue = ans;
                } else {
                    if(op.getToken().getValue().equals("*")){
                        lastvalue = lastvalue * ans;
                    } else if(op.getToken().getValue().equals("/")) {
                        lastvalue = lastvalue / ans;
                    } else {
                        lastvalue = lastvalue % ans;
                    }
                }
            } else {
                op = vn;
            }
        }
        return lastvalue;
    }

}
