package frontend.vn;

public class Cond extends Vn{

    public Cond(){
        super("<Cond>");
    }
    public int RCond(){
        LOrExp lOrExp = new LOrExp();
        lOrExp.RLOrExp();
        this.addVn(lOrExp);
        return 0;
    }
}
