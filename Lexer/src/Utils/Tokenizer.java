package Utils;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public List<List<String>> tokenize(List<String> rawCode,Judgement judgement) {
        List<List<String>> wordList = new ArrayList<>();  //因为代码长度不定，每行代码保存为一个List<String>，
        // 所有List再保存到List中
        //Judgement judgement = new Judgement();
        StringBuilder sb = new StringBuilder();

        for (String segment : rawCode) {
            char[] charArray = segment.trim().toCharArray();  //去除一行代码首尾的空白符(\t,\n等)
            // 并按字符分割保存为char数组
            List<String> wordInLine = new ArrayList<>();

            for (int i = 0; i < charArray.length; i++) {

                //当前字符为空格、分隔符(逗号、分号、前后括号等)或者运算符，则以此为分割
                //否则，读取内容即为关键词、标识符或常数，需要继续读取
                if (judgement.isSeparator(charArray[i] + "") ||
                        judgement.isOperator(charArray[i] + "") ||
                        judgement.isBlank(charArray[i])) {

                    //若当前已保存内容，则加入List
                    if (sb.length() != 0)
                        wordInLine.add(sb.toString());

                    //以下开始对分割符号进行存储
                    //因为Judgement中保存的分隔符只有一个字符，而运算符为一个或两个
                    //故对其后一位也进行判断
                    if (!judgement.isBlank(charArray[i])) {
                        if (i + 1 < charArray.length &&
                                judgement.isOperator(charArray[i] + "" + charArray[i + 1])) {
                            wordInLine.add(charArray[i] + "" + charArray[i + 1]);
                            i++;
                        } else
                            wordInLine.add(String.valueOf(charArray[i]));
                    }

                    sb.delete(0, sb.length());
                } else {
                    sb.append(charArray[i]);
                }
            }

            //当前行遍历之后，确保以全部存入
            //否则会出现非分割符号结尾时不存入的情况
            if (sb.length() != 0) {
                wordInLine.add(sb.toString());
                sb.delete(0, sb.length());
            }

            wordList.add(wordInLine);
        }
        return wordList;
    }
}
