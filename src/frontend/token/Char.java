package frontend.token;

public class Char {
    private char value;
    private int status = 0;

    public Char(char value){
        this.value = value;

    }

    public Char(){
        this.value = ' ';
    }

    public char getValue() {
        return this.value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public boolean isEOF(){
        if(this.status == -1){
            return true;
        }
        return false;
    }

    public boolean isR(){
        if(this.value == '\r'){
            return true;
        }
        return false;
    }

    public boolean isU(){
        if(this.value == '_'){
            return true;
        }
        return false;
    }

    public boolean isSpace(){
        if(this.value == ' '){
            return true;
        }
        return false;
    }

    public boolean isNewline(){
        if(this.value == '\n'){
            return true;
        }
        return false;
    }

    public boolean isTab(){
        if(this.value == '\t'){
            return true;
        }
        return false;
    }

    public boolean isUpperLetter(){
        if(this.value >= 'A' && this.value <= 'Z'){
            return true;
        }
        return false;
    }

    public boolean isLowerLetter(){
        if(this.value >= 'a' && this.value <= 'z'){
            return true;
        }
        return false;
    }

    public boolean isLetter(){
        if(this.isLowerLetter() || this.isUpperLetter()){
            return true;
        }
        return false;
    }

    public boolean isDigit(){
        if(this.value >= '0' && this.value <= '9'){
            return true;
        }
        return false;
    }

    public boolean isQuot(){
        if(this.value == '"'){
            return true;
        }
        return false;
    }

    public boolean isNot(){
        if(this.value == '!'){
            return true;
        }
        return false;
    }

    public boolean isAnd(){
        if(this.value == '&'){
            return true;
        }
        return false;
    }

    public boolean isOr(){
        if(this.value == '|'){
            return true;
        }
        return false;
    }

    public boolean isComma(){
        if(this.value == ','){
            return true;
        }
        return false;
    }

    public boolean isSemi(){
        if(this.value == ';'){
            return true;
        }
        return false;
    }

    public boolean isEqual(){
        if(this.value == '='){
            return true;
        }
        return false;
    }

    public boolean isPlus(){
        if(this.value == '+'){
            return true;
        }
        return false;
    }

    public boolean isMinus(){
        if(this.value == '-'){
            return true;
        }
        return false;
    }

    public boolean isStar(){
        if(this.value == '*'){
            return true;
        }
        return false;
    }

    public boolean isMulti(){
        if(this.value == '*'){
            return true;
        }
        return false;
    }

    public boolean isDivi(){
        if(this.value == '/'){
            return true;
        }
        return false;
    }

    public boolean isMod(){
        if(this.value == '%'){
            return true;
        }
        return false;
    }

    public boolean isLss(){
        if(this.value == '<'){
            return true;
        }
        return false;
    }

    public boolean isGre(){
        if(this.value == '>'){
            return true;
        }
        return false;
    }

    public boolean isLpar(){
        if(this.value == '('){
            return true;
        }
        return false;
    }

    public boolean isRpar(){
        if(this.value == ')'){
            return true;
        }
        return false;
    }

    public boolean isLBrack(){
        if(this.value == '['){
            return true;
        }
        return false;
    }

    public boolean isRBrack(){
        if(this.value == ']'){
            return true;
        }
        return false;
    }

    public boolean isLBrace(){
        if(this.value == '{'){
            return true;
        }
        return false;
    }

    public boolean isRBrace(){
        if(this.value == '}'){
            return true;
        }
        return false;
    }
}
