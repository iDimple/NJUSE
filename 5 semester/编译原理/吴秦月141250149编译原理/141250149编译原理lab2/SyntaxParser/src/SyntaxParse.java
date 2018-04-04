import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alisa on 16/11/8.
 */
public class SyntaxParse {
    private static ArrayList<Token> tokens;
    private static ArrayList<String> output = new ArrayList<>();
    private static Queue queue;
    private static Stack stack;
    private static String[] generations = {
            "S->id=A;", "S->if(C){S}else{S}", "S->while(C){S}", "A->TB",
            "B->+TB", "B->ε", "T->FG", "G->*FG", "G->ε", "F->(A)", "F->num",
            "F->id", "C->DE", "E->||DE", "E->ε", "D->(C)", "D->id==num"};
    private static int[][] table = {
      //   id	=	;	if	(	)	{	} else while +	* num	||	==	$
            {0,	-1,	-1,	1,	-1,	-1,	-1,	-1,	-1,	2,	-1,	-1,	-1,	-1,	-1,	-1},//S
            {3,	-1,	-1,	-1,	3,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	3,	-1,	-1,	-1},//A
            {-1,-1,	5,	-1,	-1,	5,	-1,	-1,	-1,	-1,	4,	-1,	-1,	-1,	-1,	5},//B
            {6,	-1,	-1,	-1,	6,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	6,	-1,	-1,	-1},//T
            {-1,-1,	8,	-1,	-1,	8,	-1,	-1,	-1,	-1,	8,	7,	-1,	-1,	-1,	8},//G
            {11,-1,	-1,	-1,	9,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	10,	-1,	-1,	-1},//F
            {12,-1,	-1,	-1,	12,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1},//C
            {-1,-1,	-1,	-1,	-1,	14,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	13,	-1,	14},//E
            {16,-1,	-1,	-1,	15,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1}//D
    };

    private static void output(){
        try {
            File outputFile = new File("output");
            if (!outputFile.exists())
                outputFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, false));
            for(String s:output){
                System.out.println(s);
                bw.write(s);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void parse(){
        Token t1 = null;
        Token t2 = null;

        while(queue.get().getCode()!=Token.END){
            t1 = stack.get();
            t2 = queue.get();
            if(t1.getCode()>99){//非终结符
                if(!generate(t1, t2.getCode())){
                    System.out.println("Error1!");
                    return;
                }
            }
            else{//终结符
                if(t1.getCode()==t2.getCode()){//匹配成功
                    stack.pop();
                    queue.dequeue();
                }
                else{//否则报错
                    System.out.println("Error2!");
                    return;
                }
            }
        }
        System.out.println("Done!");
    }

    private static boolean generate(Token nts,int next){
        try {
            int gi = table[nts.getCode()-100][getHeadIndex(next)];//查表
            if(gi<0){
                System.out.println("Error3!");
                return false;
            }
            output.add(generations[gi]);//产生式添加到输出队列
            stack.pop();
            switch (gi) {
                case 0:
                    stack.push(new Token(Token.SEMICOLON,";"));
                    stack.push(new Token(Token.E, "E"));
                    stack.push(new Token(Token.EQUAL, "="));
                    stack.push(new Token(Token.ID, "id"));
                    break;
                case 1:
                    stack.push(new Token(Token.RIGHT_BRACE,"}"));
                    stack.push(new Token(Token.S,"S"));
                    stack.push(new Token(Token.LEFT_BRACE,"{"));
                    stack.push(new Token(Token.ELSE, "else"));
                    stack.push(new Token(Token.RIGHT_BRACE,"}"));
                    stack.push(new Token(Token.S,"S"));
                    stack.push(new Token(Token.LEFT_BRACE,"{"));
                    stack.push(new Token(Token.RIGHT_PARENTHESE,")"));
                    stack.push(new Token(Token.C,"C"));
                    stack.push(new Token(Token.LEFT_PARENTHESE,"("));
                    stack.push(new Token(Token.IF,"if"));
                    break;
                case 2:
                    stack.push(new Token(Token.RIGHT_BRACE,"}"));
                    stack.push(new Token(Token.S,"S"));
                    stack.push(new Token(Token.LEFT_BRACE,"{"));
                    stack.push(new Token(Token.RIGHT_PARENTHESE,")"));
                    stack.push(new Token(Token.C,"C"));
                    stack.push(new Token(Token.LEFT_PARENTHESE,"("));
                    stack.push(new Token(Token.WHILE,"while"));
                    break;
                case 3:
                    stack.push(new Token(Token.E1,"E'"));
                    stack.push(new Token(Token.T,"T"));
                    break;
                case 4:
                    stack.push(new Token(Token.E1,"E'"));
                    stack.push(new Token(Token.T,"T"));
                    stack.push(new Token(Token.ADD,"+"));
                    break;
                case 5:
                    break;
                case 6:
                    stack.push(new Token(Token.T1,"T'"));
                    stack.push(new Token(Token.F,"F"));
                    break;
                case 7:
                    stack.push(new Token(Token.T1,"T'"));
                    stack.push(new Token(Token.F,"F"));
                    stack.push(new Token(Token.MUL,"*"));
                    break;
                case 8:
                    break;
                case 9:
                    stack.push(new Token(Token.RIGHT_PARENTHESE,")"));
                    stack.push(new Token(Token.E,"E"));
                    stack.push(new Token(Token.LEFT_PARENTHESE,"("));
                    break;
                case 10:
                    stack.push(new Token(Token.NUMBER,"num"));
                    break;
                case 11:
                    stack.push(new Token(Token.ID,"id"));
                    break;
                case 12:
                    stack.push(new Token(Token.C1,"C'"));
                    stack.push(new Token(Token.D,"D"));
                    break;
                case 13:
                    stack.push(new Token(Token.C1,"C'"));
                    stack.push(new Token(Token.D,"D"));
                    stack.push(new Token(Token.DOUBLE_OR,"||"));
                    break;
                case 14:
                    break;
                case 15:
                    stack.push(new Token(Token.RIGHT_PARENTHESE,")"));
                    stack.push(new Token(Token.C,"C"));
                    stack.push(new Token(Token.LEFT_PARENTHESE,"("));
                    break;
                case 16:
                    stack.push(new Token(Token.NUMBER,"num"));
                    stack.push(new Token(Token.DOUBLE_EQUAL,"=="));
                    stack.push(new Token(Token.ID,"id"));
                    break;
                default:
                    System.out.println("Error4!");
                    return false;
            }
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error5!");
            return false;
        }
    }

    private static int getHeadIndex(int code){
        switch (code) {
            case 56: return 0;
            case 30: return 1;
            case 53: return 2;
            case 12: return 3;
            case 45: return 4;
            case 46: return 5;
            case 49: return 6;
            case 50: return 7;
            case 13: return 8;
            case 15: return 9;
            case 22: return 10;
            case 26: return 11;
            case 57: return 12;
            case 35: return 13;
            case 31: return 14;
            case 0: return 15;
            default: return -1;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
//		readTable();
        tokens = Analyzer.getTokens();
        queue = new Queue(tokens);
        stack = new Stack();
        stack.push(new Token(Token.S, "S"));
        parse();
        output();
    }
}
