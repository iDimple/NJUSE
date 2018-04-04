/**
 * Created by Alisa on 16/10/22.
 */
public class Token {
    //标识码
    private String code;
    //单词符号
    private String str;

    private String error;

    public Token(String code,String str){
       this.code=code;
        this.str=str;
        this.error=null;
    }

    public Token(String error){
        this.error=error;
    }

    public String toString(){
        if(this.error!=null){
            return "Error:"+this.error;
        }
        return this.code+" : "+this.str;
    }
}
