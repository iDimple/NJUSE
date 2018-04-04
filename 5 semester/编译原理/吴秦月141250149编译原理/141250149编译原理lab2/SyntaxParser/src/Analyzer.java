import java.io.*;
import java.util.ArrayList;

/**
 * Created by Alisa on 16/11/8.
 */
public class Analyzer {

    private static char input[] = new char[500];// 存储输入的字符数组
    private static ArrayList<Token> output = new ArrayList<>();//输出
    private static char word[];// 单词符号
    private static int num;// 常数
    private static int code;// 单词种别码
    private static char ch;// 当前读的字符
    private static int p, sp, row;
    private static String inputFile;

    // 以下是可识别的单词符号
    private static String[] reservedWords = { "class", "public", "protected",
            "private", "void", "static", "int", "char", "float", "double",
            "string", "if", "else", "do", "while", "try", "catch", "switch",
            "case" ,"for" };

//	 private static String[] operators =
//	 {"+","+=","-","-=","*","*=","/","/=","=","==","&","|","&&","||","!","!="
//	 ,"<","<=",">",">="};
//	 private static String[] notes = {"//","/*","*/"};
//	 private static String[] others =
//	 {"(",")","[","]","{","}",";",",",":","'","\""};

    private static void getInput() throws IOException {
        inputFile = "input.txt";
        BufferedReader br2 = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(inputFile))));
        String line = null;
        char[] tmp = null;
        p = 0;
        while ((line = br2.readLine()) != null) {
            tmp = line.toCharArray();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i] == ' '|| tmp[i]=='\t')
                    continue;
                input[p++] = tmp[i];
            }
            input[p++] = '\n';
        }
        input[p] = '#';
        br2.close();
    }

    @SuppressWarnings("unused")
    private static void output() throws IOException{

        String[] splits = inputFile.split("\\.");
        File outputFile = new File(splits[0]+".output");
        if (!outputFile.exists())
            outputFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, false));
        for(Token t:output){
            System.out.println(t.toString());
            bw.write(t.toString());
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    private static void scanner() {
        word = new char[20];
        ch = input[p++];

        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {// 可能是保留字或变量名（保留字优先）
            sp = 0;
            while ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z')
                    || (ch >= 'A' && ch <= 'Z')) {
                word[sp++] = ch;
                ch = input[p++];
                word[sp] = '\0';
                for (int i = 0; i < reservedWords.length; i++) {
                    if (ch2s(word).equals(reservedWords[i])) {
                        code = i + 1;
                        p --;
                        return;
                    }
                }
            }
            word[sp++] = '\0';
            p--; // 放回多读的
            code = 56;

        } else if (ch >= '0' && ch <= '9') { // 可能是正常数
            num = 0;
            while (ch >= '0' && ch <= '9') {
                num = num * 10 + ch - '0';
                ch = input[p++];
            }
            p--;
            code = 57;
            if (num < 0)
                code = -2;// 正数超过最大值变成负数，报错
        } else { // 其他字符
            sp = 0;
            word[sp++] = ch;
            switch (ch) {
                case '+':
                    ch = input[p++];
                    if (ch == '=') {// +=
                        code = 23;
                        word[sp++] = ch;
                    } else {// +
                        code = 22;
                        p--;
                    }
                    break;
                case '-':
                    ch = input[p++];
                    if (ch >= '0' && ch <= '9') { // 可能是负常数
                        num = 0;
                        while (ch >= '0' && ch <= '9') {
                            num = num * 10 + ch - '0';
                            ch = input[p++];
                        }
                        p--;
                        code = 57;
                        if (num < 0)
                            code = -2;
                        num *= -1;// 变成负数
                    } else if (ch == '=') {// -=
                        code = 25;
                        word[sp++] = ch;
                    } else {// -
                        code = 24;
                        p--;
                    }
                    break;
                case '*':
                    ch = input[p++];
                    if (ch == '=') {// *=
                        code = 27;
                        word[sp++] = ch;
                    } else if (ch == '/') {// */
                        code = 44;
                        word[sp++] = ch;
                    } else {// *
                        code = 26;
                        p--;
                    }
                    break;
                case '/':
                    ch = input[p++];
                    if (ch == '=') {// /=
                        code = 29;
                        word[sp++] = ch;
                    } else if (ch == '/') {// //
                        code = 42;
                        word[sp++] = ch;
                    } else if (ch == '*') {// /*
                        code = 26;
                        word[sp++] = ch;
                    } else {// /
                        code = 28;
                        p--;
                    }
                    break;
                case '=':
                    ch = input[p++];
                    if (ch == '=') { // ==
                        code = 31;
                        word[sp++] = ch;
                    } else { // =
                        code = 30;
                        p--;
                    }
                    break;
                case '<':
                    ch = input[p++];
                    if (ch == '=') { // <=
                        code = 39;
                        word[sp++] = ch;
                    } else { // <
                        code = 38;
                        p--;
                    }
                    break;
                case '>':
                    ch = input[p++];
                    if (ch == '=') { // >=
                        code = 41;
                        word[sp++] = ch;
                    } else { // >
                        code = 40;
                        p--;
                    }
                    break;
                case '&':
                    ch = input[p++];
                    if (ch == '&') { // &&
                        code = 33;
                        word[sp++] = ch;
                    } else { // &
                        code = 32;
                        p--;
                    }
                    break;
                case '|':
                    ch = input[p++];
                    if (ch == '|') { // ||
                        code = 35;
                        word[sp++] = ch;
                    } else { // |
                        code = 34;
                        p--;
                    }
                    break;
                case '!':
                    ch = input[p++];
                    if (ch == '=') { // !=
                        code = 37;
                        word[sp++] = ch;
                    } else { // !
                        code = 36;
                        p--;
                    }
                    break;

                case '(':code = 45; break;
                case ')':code = 46; break;
                case '[':code = 47; break;
                case ']':code = 48; break;
                case '{':code = 49; break;
                case '}':code = 50; break;
                case ',':code = 51; break;
                case ':':code = 52; break;
                case ';':code = 53; break;
                case '\'':code = 54; break;
                case '\"':code = 55; break;
                case '\n':code = -1; break;
                default:code = -3; break;
            }
        }
    }

    private static String ch2s(char[] c) {
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i] != '\0')
                len++;
        }
        return String.valueOf(c).substring(0, len);
    }

    public static ArrayList<Token> getTokens(){
        try {
            getInput();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        p = 0;
        row = 1;
        do {
            scanner();
            switch (code) {
                case 57: // 常数
                    output.add(new Token(code, num+""));
                    break;
                case -1: // 换行
                    row++;
                    break;
                case -2: // 整型过大
                    output.add(new Token("integer overflow at row "+row));
                    break;
                case -3: // 未定义字符
                    output.add(new Token("undefined character at row "+row));
                    break;
                default:// 一般单词符号
                    output.add(new Token(code,ch2s(word)));
                    break;
            }
        } while (input[p] != '#');
//		try {
//			output();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        return output;
    }


}
