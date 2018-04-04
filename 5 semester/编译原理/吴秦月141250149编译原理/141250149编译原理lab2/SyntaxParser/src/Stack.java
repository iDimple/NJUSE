import java.util.ArrayList;

/**
 * Created by Alisa on 16/11/8.
 */
public class Stack {
    private ArrayList<Token> stack;

    public Stack(){
        stack = new ArrayList<Token>();
        stack.add(new Token(Token.END,"$"));
    }

    public void push(Token t){
        stack.add(t);
//		print();
    }

    public void pop(){
        stack.remove(stack.size() - 1);
    }

    public Token get(){
        return stack.get(stack.size() - 1);
    }

    private void print(){
        for(int i=stack.size()-1;i>=0;i--){
            System.out.println(stack.get(i));
        }
        System.out.println();
    }
}
