import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alisa on 16/10/22.
 */
public class Analyzer {
    //存储输入的字符数组
    private static char input[] = new char[600];
    private static int p = 0;//input下标
    //存储输出的token 序列
    private static ArrayList<Token> output = new ArrayList<>();
    //单词符号
    private static char word[];
    private static char ch;// 当前读的字符
    private static String code;// 单词种别码
    private static int sp, row;
    private static int num;
    private static String inputFile;

    public static void main(String[] args) {
        inputFile = "input.txt";
        p = 0;
        try {
            input = IOHelper.readFile(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        do {
            scanner();
            switch (code) {
                case "整数":
                    output.add(new Token(code, num + ""));
                    break;
                case "数字太大溢出":
                    output.add(new Token("integer overflow"));
                    break;
                case "错误":
                    output.add(new Token("undefined character "));
                    break;
                case "回车":
                    break;
                default:
                    output.add(new Token(code, charToString(word)));
                    break;
            }
        } while (input[p] != '#');

        try {
            IOHelper.output(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String[] keyWords = {"class", "public", "protected",
            "private", "void", "static", "int", "char", "float", "double",
            "if", "else", "do", "while", "try", "catch", "switch",
            "case", "for", "main", "String","boolean","true","false"};

    private static void scanner() {
        word = new char[20];
        ch = input[p++];
       // System.out.println("aaa"+ch);
        if (isAlphabet(ch)) {// 可能是保留字或变量名（保留字优先）
            sp = 0;
            while (isDigit(ch) || isAlphabet(ch)) {
                word[sp++] = ch;
                ch = input[p++];
                word[sp] = '\0';
                for (int i = 0; i < keyWords.length; i++) {
                    if (charToString(word).equals(keyWords[i])) {
                        code = "关键字";
                        p--;
                        return;
                    }
                }
            }
            word[sp++] = '\0';
            p--; // 放回多读的
            code = "标识符";

        } else if (isDigit(ch)) { // 可能是正常数
            num = 0;
            while (isDigit(ch)) {
                num = num * 10 + ch - '0';
                ch = input[p++];
            }
            p--;
            code = "整数";
            if (num < 0)
                code = "数字太大溢出";// 正数超过最大值变成负数，报错
        } else { // 其他字符
            sp = 0;
            word[sp++] = ch;
            switch (ch) {
                case '+':
                    ch = input[p++];
                    if (ch == '=') {// +=
                        code = "操作符";
                        word[sp++] = ch;
                    } else {// +
                        code = "操作符";
                        p--;
                    }
                    break;
                case '-':
                    ch = input[p++];
                    if (isDigit(ch)) { // 可能是负常数
                        num = 0;
                        while (isDigit(ch)) {
                            num = num * 10 + ch - '0';
                            ch = input[p++];
                        }
                        p--;
                        code = "整数";
                        if (num < 0)
                            code = "数字太大溢出";
                        num *= -1;// 变成负数
                    } else if (ch == '=') {// -=
                        code = "操作符";
                        word[sp++] = ch;
                    } else {// -
                        code = "操作符";
                        p--;
                    }
                    break;
                case '*':
                    ch = input[p++];
                    if (ch == '=') {// *=
                        code = "操作符";
                        word[sp++] = ch;
                    } else if (ch == '/') {// */
                        code = "注释符";
                        word[sp++] = ch;
                    } else {// *
                        code = "操作符";
                        p--;
                    }
                    break;
                case '/':
                    ch = input[p++];
                    if (ch == '/') {// //
                        code = "注释符";
                        word[sp++] = ch;
                    } else if (ch == '*') {// /*
                        code = "注释符";
                        word[sp++] = ch;
                    } else {// /
                        code = "操作符";
                        p--;
                    }
                    break;
                case '=':
                    ch = input[p++];
                    if (ch == '=') { // ==
                        code = "操作符";
                        word[sp++] = ch;
                    } else { // =
                        code = "操作符";
                        p--;
                    }
                    break;
                case '<':
                    ch = input[p++];
                    if (ch == '=') { // <=
                        code = "操作符";
                        word[sp++] = ch;
                    } else { // <
                        code = "操作符";
                        p--;
                    }
                    break;
                case '>':
                    ch = input[p++];
                    if (ch == '=') { // >=
                        code = "操作符";
                        word[sp++] = ch;
                    } else { // >
                        code = "操作符";
                        p--;
                    }
                    break;
                case '&':
                    ch = input[p++];
                    if (ch == '&') { // &&
                        code = "操作符";
                        word[sp++] = ch;
                    } else { // &
                        code = "操作符";
                        p--;
                    }
                    break;
                case '|':
                    ch = input[p++];
                    if (ch == '|') { // ||
                        code = "操作符";
                        word[sp++] = ch;
                    } else { // |
                        code = "操作符";
                        p--;
                    }
                    break;
                case '!':
                    ch = input[p++];
                    if (ch == '=') { // !=
                        code = "操作符";
                        word[sp++] = ch;
                    } else { // !
                        code = "操作符";
                        p--;
                    }
                    break;

                case '(':
                    code = "操作符";
                    break;
                case ')':
                    code = "操作符";
                    break;
                case '[':
                    code = "操作符";
                    break;
                case ']':
                    code = "操作符";
                    break;
                case '{':
                    code = "分隔符";
                    break;
                case '}':
                    code = "分隔符";
                    break;
                case ',':
                    code = "分隔符";
                    break;
                case ';':
                    code = "分隔符";
                    break;
                case '\'':
                    code = "操作符";
                    break;
                case '\"':
                    code = "操作符";
                    break;
                case '\n':
                    code = "回车";
                    break;
                case '.':
                    code = "操作符";
                default:
                    code = "错误";
                    break;
            }
        }
    }

    private static boolean isDigit(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    private static boolean isAlphabet(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return true;
        }
        return false;
    }

    private static String charToString(char[] c) {
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i] != '\0') {
                len++;
            }
        }
        return String.valueOf(c).substring(0, len);
    }
}
