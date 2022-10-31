package frontend.vn;

public class ConstExp extends Vn{

    public ConstExp(){
        super("<frontend.vn.ConstExp>");
    }
    public int RConstExp(){
        AddExp addExp = new AddExp();
        addExp.RAddExp();
        this.addVn(addExp);
        return 0;
    }
}
