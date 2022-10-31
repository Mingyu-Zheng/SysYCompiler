package frontend.token;

import java.util.HashMap;

public enum TokenType{

    IDENFR("IDENFR",""),
    INTCON("INTCON",""),
    STRCON("STRCON",""),
    MAINTK("MAINTK","main"),
    CONSTTK("CONSTTK","const"),
    INTTK("INTTK","int"),
    BREAKTTK("BREAKTK","break"),
    CONTINUETK("CONTINUETK","continue"),
    IFTK("IFTK","if"),
    ELSETK("ELSETK","else"),
    NOT("NOT","!"),
    AND("AND","&&"),
    OR("OR","||"),
    WHILETK("WHILETK","while"),
    GETINTTK("GETINTTK","getint"),
    PRINTFTK("PRINTFTK","printf"),
    RETURNTK("RETURNTK","return"),
    PLUS("PLUS","+"),
    MINU("MINU","-"),
    VOIDTK("VOIDTK","void"),
    MULT("MULT","*"),
    DIV("DIV","/"),
    MOD("MOD","%"),
    LSS("LSS","<"),
    LEQ("LEQ","<="),
    GRE("GRE",">"),
    GEQ("GEQ",">="),
    EQL("EQL","=="),
    NEQ("NEQ","!="),
    ASSIGN("ASSIGN","="),
    SEMICN("SEMICN",";"),
    COMMA("COMMA",","),
    LPARENT("LPARENT","("),
    RPARENT("RPARENT",")"),
    LBRACK("LBRACK","["),
    RBRACK("RBRACK","]"),
    LBRACE("LBRACE","{"),
    RBRACE("RBRACE","}");

    private String word;
    private String name;

    private static HashMap<String, TokenType> reserveMap = new HashMap<String, TokenType>() {{
        TokenType[] values = TokenType.values();
        for(TokenType type:values){
            if(!type.getWord().isEmpty()){
                put(type.getWord(),type);
            }
        }
    }};

    private TokenType(String name, String word){
        this.name = name;
        this.word = word;
    }

    public String getWord(){
        return this.word;
    }

    public String getName(){
        return this.name;
    }

    public static boolean isType(String str, TokenType t){
        if(!str.isEmpty()){
            if(str.equals(t.word)){
                return true;
            }
            if(t == TokenType.IDENFR || t == TokenType.INTCON || t == TokenType.STRCON){
                return true;
            }
        }
        return false;
    }

    public static boolean isReserveWord(String str){
        if(reserveMap.containsKey(str)){
            return true;
        }
        return false;
    }

    public static TokenType typeReserveWord(String str){
        return reserveMap.get(str);
    }

}
