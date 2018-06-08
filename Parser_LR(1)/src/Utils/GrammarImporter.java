package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrammarImporter {

    private List<String> grammar = new ArrayList<>();  //用于在打开文件时保存读取的内容，方便之后取用

    public List<String> importGrammar(File file) throws IOException {
        BufferedReader in = new BufferedReader(new java.io.FileReader(file));
        String strTemp;
        while ((strTemp = in.readLine()) != null) grammar.add(strTemp);

        in.close();
        return grammar;
    }

    //获取文法
    public List<String> getRawGrammar() {
        return grammar;
    }

}