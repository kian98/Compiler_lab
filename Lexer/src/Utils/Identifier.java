package Utils;

import Storager.LexResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Identifier {

    public List<LexResult> identify(List<List<String>> codeDivided) {
        List<LexResult> results = new ArrayList<>();
        Judgement judge = new Judgement();
        int row, line;

        for (row = 1; row <= codeDivided.size(); row++) {
            List<String> singleLine = codeDivided.get(row - 1);  //获取当前Token
            for (line = 1; line <= singleLine.size(); line++) {
                int rank = 7;  //rank用于指示当前Token类型，默认7为Error
                String str = singleLine.get(line - 1);
                char firstChar = str.charAt(0);  //获取Token首字符

                if (Character.isDigit(firstChar)) {
                    Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
                    rank = pattern.matcher(str).matches() ? 5 : 7;  //正则表达式判断是否为表示实数的字符串
                } else if (Character.isLetter(firstChar))  //判断是关键词或是标识符
                    rank = judge.isKeyword(str) ? 1 : 6;
                else if (judge.isSeparator(str))
                    rank = 2;
                else if (judge.isArithmeticOperator(str))
                    rank = 3;
                else if (judge.isRelationOperator(str))
                    rank = 4;
                results.add(new LexResult(str, rank, judge.getType(rank), row, line));
            }
        }
        return results;
    }
}
