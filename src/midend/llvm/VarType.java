package midend.llvm;

public enum VarType{

    INT("i32"),
    BOOL("i1"),
    VOID("void"),
    LABEL("label"),
    INT_POINTER("i32*"),
    ARRAY("array"),
    ;
    private String typeName;

    private VarType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName(){
        return this.typeName;
    }
}
