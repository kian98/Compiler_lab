package Utils;

import Storage.ParseResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    private static List<ParseResult> parseResults;
    private static boolean success;

    private static int stepCount;
    private static List<String> status_cur;
    private static List<String> symbolStack;
    private static List<String> textRemain;
    private static String movement;

    public static List<ParseResult> parse(String text, ParseTableCreator parseTable, List<List<String>> extendedGrammar) {
        status_cur = new ArrayList<>();
        symbolStack = new ArrayList<>();
        textRemain = new ArrayList<>();

        parseResults = new ArrayList<>();
        textRemain.addAll(Arrays.asList(text.split("")));
        init();
        while (true) {
            String sm = status_cur.get(status_cur.size() - 1);
            int statusIndex = Integer.parseInt(sm) + 1;
            String ai = textRemain.get(0);
            int colInAction = searchStringArray(parseTable.ACTION.get(0), ai);
            String indicate = parseTable.ACTION.get(statusIndex)[colInAction];
            if (indicate.equals("acc")) {
                movement = "Acc:分析成功";
                success = true;
                break;
            } else if (indicate.equals(" ")) {
                movement = "Err:分析失败";
                success = false;
                break;
            } else if (indicate.charAt(0) == 's') {
                String nextIndex = indicate.substring(1);
                movement = "ACTION[" + sm + "," + ai + "]=S" + nextIndex + ",状态" + nextIndex + "入栈";
                addToResults();
                status_cur.add(nextIndex);
                symbolStack.add(textRemain.get(0));
                textRemain.remove(0);
            } else if (indicate.charAt(0) == 'r') {
                String reduceLeft = extendedGrammar.get(0).get(Integer.parseInt(indicate.substring(1)));
                String reduceRight = extendedGrammar.get(1).get(Integer.parseInt(indicate.substring(1)));
                int reduceSize = reduceRight.length();
                String newStatus = status_cur.get(status_cur.size() - 1 - reduceSize);      //获取归约后最末项

                int colInGOTO = searchStringArray(parseTable.GOTO.get(0), reduceLeft);    //GOTO列,A
                statusIndex = Integer.parseInt(newStatus) + 1;     //GOTO行,S(m-r)
                String gotoIndex = parseTable.GOTO.get(statusIndex)[colInGOTO];     //GOTO中对应的GOTO[S(m-r),A]

                movement = indicate + ": " + reduceLeft + "->" + reduceRight + "归约, " + "GOTO(" + newStatus + "," + reduceLeft + ")=" + gotoIndex + " 入栈";
                addToResults();

                symbolStack = symbolStack.subList(0, symbolStack.size() - reduceSize + 1);
                symbolStack.add(reduceLeft);
                status_cur = status_cur.subList(0, status_cur.size() - reduceSize);
                status_cur.add(gotoIndex);
            }
            stepCount++;
        }
        addToResults();

        return parseResults;
    }

    private static void init() {
        stepCount = 1;
        status_cur.add("0");
        symbolStack.add("#");
    }

    private static void addToResults() {
        parseResults.add(new ParseResult(stepCount, combine(status_cur, ","),
                combine(symbolStack, ""), combine(textRemain, ""), movement));
    }

    private static String combine(List<String> list, String delimiter) {
        return String.join(delimiter, list);
    }

    private static int searchStringArray(String array[], String target) {
        int index = -1;
        for (int i = 0; i < array.length; i++)
            if (array[i].equals(target)) {
                index = i;
                break;
            }
        return index;
    }

    public static boolean isSuccess() {
        return success;
    }
}
