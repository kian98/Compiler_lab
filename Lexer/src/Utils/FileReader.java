package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class FileReader {

    private List<String> allCode = new ArrayList<>();  //用于在打开文件时保存读取的内容，方便之后取用

    public String readin(File file) throws IOException {
        int curLine = 1;
        BufferedReader in = new BufferedReader(new java.io.FileReader(file));
        String strTemp;
        StringBuilder sb = new StringBuilder();

        //读取文件、设置行号格式，并按行保存到List中
        while ((strTemp = in.readLine()) != null) {
            sb.append(curLine < 10 ? curLine + "  |  " : curLine + " |  ");  //对齐行号
            sb.append(strTemp + "\n");
            allCode.add(strTemp);
            curLine++;
        }

        in.close();
        return sb.toString();
    }

    //获取代码
    public List<String> rawCode() {
        return allCode;
    }
}
