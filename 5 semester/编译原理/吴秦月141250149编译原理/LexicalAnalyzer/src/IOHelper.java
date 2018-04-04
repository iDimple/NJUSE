import java.io.*;
import java.util.ArrayList;

/**
 * Created by Alisa on 16/10/22.
 */
public class IOHelper {
    public static char[] readFile(String name) throws IOException {
        String inputFile = name;
        BufferedReader br =  new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(inputFile))));
        String line = null;
        char[] tmp = null;
        char result[] = new char[600];
        int p = 0;
        while((line=br.readLine())!=null){
           tmp = line.toCharArray();//一个字母一个字母的读
            for(int i = 0;i<tmp.length;i++){
                if(tmp[i]==' '||tmp[i]=='\t') {//去掉空格和制表符
                    continue;
                }
                    result[p++]=tmp[i];
            //    System.out.println(tmp[i]);
            }
            result[p++]='\n';//手动添加换行
        }
        result[p]='#';//约定以#结尾
        br.close();
        return result;
    }

    public static void output(ArrayList<Token> output) throws IOException{


        File outputFile = new File("output.txt");
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
}
