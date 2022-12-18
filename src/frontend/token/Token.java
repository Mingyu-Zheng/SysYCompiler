package frontend.token;

import utils.Reader;
import utils.Writer;

import java.util.ArrayList;

public class Token {
    protected int line;
    protected TokenType type;
    protected String value;
    protected int numvalue = 0;

    static ArrayList<Token> tokens = new ArrayList<>();
    static int tptr = 0;


    public Token(int line, TokenType type, String value){
        this.line = line;
        this.type = type;
        this.value = value;
    }

    public Token(){
        this.value = "";
    }

    public String getValue() {
        if(this.isType(TokenType.IDENFR) || this.isType(TokenType.STRCON) || this.isType(TokenType.INTCON)){
            return value;
        } else {
            return this.type.getWord();
        }
    }

    public int getLine(){
        return this.line;
    }

    public void setLine(int line){
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type){
        this.type = type;
    }

    public boolean isType(TokenType type){
        if(this.type == type){
            return true;
        }
        return false;
    }

    public static void addToken(Token token){
        Token.tokens.add(token);
    }

    public static Token nextToken(){
        Token token = null;
        if(tptr < tokens.size()){
            token = tokens.get(tptr);
            tptr++;
        }
        return token;
    }

    public static int getTptr(){
        return Token.tptr;
    }

    public static void setTptr(int tptr){
        Token.tptr = tptr;
    }

    public static void retractToken(){
        tptr--;
    }

    public void updateNumvalue(){
        if(this.type == TokenType.INTCON){
            this.numvalue = Integer.valueOf(this.value);
        }
    }

    public void catToken(Char c){
        this.value += c.getValue();
    }

    public boolean isReserveWord(){
        return TokenType.isReserveWord(this.value);
    }


    public void updateReserveWord(){
        this.type = TokenType.typeReserveWord(this.value);
    }

    public static Token getToken(Reader reader){
        Token thetoken = new Token();
        Char thechar = new Char();
        thetoken.setLine(reader.getLptr());
        reader.getchar(thechar);
        if(thechar.isEOF()){
            return null;
        }
        while(thechar.isSpace() || thechar.isNewline() || thechar.isTab() || thechar.isR()){
            reader.getchar(thechar);
        }
        if(thechar.isEOF()){
            return null;
        }
        thetoken.setLine(reader.getLptr() + 1);
        if(thechar.isLetter() || thechar.isU()){
            while(thechar.isLetter() || thechar.isDigit() || thechar.isU()){
                thetoken.catToken(thechar);
                reader.getchar(thechar);
            }
            reader.retract();
            if(thetoken.isReserveWord()){
                thetoken.updateReserveWord();
            } else {
                thetoken.setType(TokenType.IDENFR);
            }
        }
        else if(thechar.isDigit()){
            while (thechar.isDigit()){
                thetoken.catToken(thechar);
                reader.getchar(thechar);
            }
            reader.retract();
            thetoken.setType(TokenType.INTCON);
            thetoken.updateNumvalue();
        }
        else if(thechar.isQuot()){
            thetoken.catToken(thechar);
            reader.getchar(thechar);
            while (!thechar.isQuot()){
                thetoken.catToken(thechar);
                reader.getchar(thechar);
            }
            thetoken.catToken(thechar);
            thetoken.setType(TokenType.STRCON);
        }
        else if(thechar.isEqual()){
            thetoken.catToken(thechar);
            reader.getchar(thechar);
            if(thechar.isEqual()){
                thetoken.catToken(thechar);
                thetoken.updateReserveWord();
            } else {
                thetoken.updateReserveWord();
                reader.retract();
            }
        }
        else if(thechar.isAnd()){
            thetoken.catToken(thechar);
            reader.getchar(thechar);
            if(thechar.isAnd()){
                thetoken.catToken(thechar);
                thetoken.updateReserveWord();
            }
        }
        else if(thechar.isOr()){
            thetoken.catToken(thechar);
            reader.getchar(thechar);
            if(thechar.isOr()){
                thetoken.catToken(thechar);
                thetoken.updateReserveWord();
            }
        }
        else if(thechar.isNot()){
            thetoken.catToken(thechar);
            reader.getchar(thechar);
            if(thechar.isEqual()){
                thetoken.catToken(thechar);
                thetoken.updateReserveWord();
            } else {
                thetoken.updateReserveWord();
                reader.retract();
            }
        }
        else if(thechar.isLss()){
            thetoken.catToken(thechar);
            reader.getchar(thechar);
            if(thechar.isEqual()){
                thetoken.catToken(thechar);
                thetoken.updateReserveWord();
            } else {
                thetoken.updateReserveWord();
                reader.retract();
            }
        }
        else if(thechar.isGre()){
            thetoken.catToken(thechar);
            reader.getchar(thechar);
            if(thechar.isEqual()){
                thetoken.catToken(thechar);
                thetoken.updateReserveWord();
            } else {
                thetoken.updateReserveWord();
                reader.retract();
            }
        }
        else if(thechar.isPlus() || thechar.isMinus() || thechar.isMulti() || thechar.isSemi() ||
                thechar.isMod() || thechar.isComma() || thechar.isLpar() || thechar.isRpar() ||
                thechar.isLBrack() || thechar.isRBrack() || thechar.isLBrace() || thechar.isRBrace()){
            thetoken.catToken(thechar);
            thetoken.updateReserveWord();
        }
        else if(thechar.isDivi()){
            thetoken.catToken(thechar);
            reader.getchar(thechar);
            if(thechar.isStar()){
                do{
                    do {
                        reader.getchar(thechar);
                    } while (!(thechar.isStar() || thechar.isEOF()));
                    do {
                        reader.getchar(thechar);
                        if(thechar.isDivi()){
                            break;
                        }
                    } while (thechar.isStar());
                } while(!(thechar.isStar() || thechar.isDivi() || thechar.isEOF()));
                return getToken(reader);
            }
            else if(thechar.isDivi()){
                do {
                    reader.getchar(thechar);
                }while (!(thechar.isNewline() || thechar.isEOF()));
                return getToken(reader);
            }
            else{
                thetoken.updateReserveWord();
                reader.retract();
            }
        }
        return thetoken;
    }

    public static void writeTokens(Writer writer){
        for(Token token:tokens){
            // System.out.print(token.getType().getName() + " " + token.getValue() + "\n");
            writer.addStr(token.getType().getName() + " " + token.getValue() + "\n");
        }
    }
}

