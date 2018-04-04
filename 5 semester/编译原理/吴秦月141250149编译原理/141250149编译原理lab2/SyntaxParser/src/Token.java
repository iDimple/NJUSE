/**
 * Created by Alisa on 16/11/8.
 */
public class Token {
    public static final int CLASS = 1;
    public static final int PUBLIC = 2;
    public static final int PROTECTED = 3;
    public static final int PRIVATE = 4;
    public static final int VOID = 5;
    public static final int STATIC = 6;
    public static final int INT = 7;
    public static final int CHAR = 8;
    public static final int FLOAT = 9;
    public static final int DOUBLE = 10;
    public static final int STRING = 11;
    public static final int IF = 12;
    public static final int ELSE = 13;
    public static final int DO = 14;
    public static final int WHILE = 15;
    public static final int TRY = 16;
    public static final int CATCH = 17;
    public static final int SWITCH = 18;
    public static final int CASE = 19;
    public static final int FOR = 20;
    public static final int ADD = 22;
    public static final int ADD_EQUAL = 23;
    public static final int MIN = 24;
    public static final int MIN_EQUAL = 25;
    public static final int MUL = 26;
    public static final int MUL_EQUAL = 27;
    public static final int DIV = 28;
    public static final int DIV_EQUAL = 29;
    public static final int EQUAL = 30;
    public static final int DOUBLE_EQUAL = 31;
    public static final int AND = 32;
    public static final int DOUBLE_AND = 33;
    public static final int OR = 34;
    public static final int DOUBLE_OR = 35;
    public static final int NOT = 36;
    public static final int NOT_EQUAL = 37;
    public static final int LESS = 38;
    public static final int LESS_EQUAL = 39;
    public static final int GREATER = 40;
    public static final int GREATER_EQUAL = 41;
    public static final int DOUBLE_SLASH = 42;
    public static final int SLASH_STAR = 43;
    public static final int STAR_SLASH = 44;
    public static final int LEFT_PARENTHESE = 45;
    public static final int RIGHT_PARENTHESE = 46;
    public static final int LEFT_BRACKET = 47;
    public static final int RIGHT_BRACKET = 48;
    public static final int LEFT_BRACE = 49;
    public static final int RIGHT_BRACE = 50;
    public static final int COMMA = 51;
    public static final int COLON = 52;
    public static final int SEMICOLON = 53;
    public static final int SINGLE_QUOTE = 54;
    public static final int DOUBLE_QUOTE = 55;
    public static final int ID = 56;
    public static final int NUMBER = 57;
    public static final int END = 0;
    public static final int ERROR = -1;

    public static final int S = 100;
    public static final int E = 101;
    public static final int E1 = 102;
    public static final int T = 103;
    public static final int T1 = 104;
    public static final int F = 105;
    public static final int C = 106;
    public static final int C1 = 107;
    public static final int D = 108;

    private int code;
    private String str;
    private String error;

    public Token(int c,String s){
        this.code = c;
        this.str = s;
        this.error = null;
    }

    public Token(String error){
        this.error = error;
        this.code = ERROR;
    }

    public String toString(){
        if(this.error != null)
            return "Error:" + this.error;
        return "<" + this.code + "," + this.str + ">";
    }

    public String getStr(){
        return str;
    }

    public int getCode(){
        return code;
    }
}
