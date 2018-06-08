package Utils;

import Storage.ParseResult;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static List<ParseResult> parseResults;
    private static boolean success;

    public static List<ParseResult> parse(Closure closureList, List<List<String>> extendedGrammar){
        parseResults =new ArrayList<>();



        return parseResults;
    }

    public static boolean isSuccess() {
        return success;
    }
}
