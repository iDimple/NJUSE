import java.util.ArrayList;

/**
 * Created by Alisa on 16/11/8.
 */
public class Queue {
    private ArrayList<Token> line;

    public Queue(ArrayList<Token> list){
        this.line = list;
        this.line.add(new Token(Token.END,"$"));
    }

    public Token get(){
        return line.get(0);
    }

    public void dequeue(){
//		Token ret = line.get(0);
        line.remove(0);
//		return ret;
    }

    public void enqueue(Token token){
        this.line.add(token);
    }
}
