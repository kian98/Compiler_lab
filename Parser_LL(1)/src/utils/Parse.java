package utils;

import Storage.Parser;
import sun.awt.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parse {
    boolean success;
    boolean flag;
    List<Parser> parseStep = new ArrayList<>();
    int step = 0;
    String parseProcess;
    String formula;
    String movement;
    String analyzeText;
    List<List<String>> analysisResult;
    List<String> allVn;
    List<String> allVt;
    List<String> VtInM;
    List<List<String[]>> vnInfer_separated;
    Stack<String> stack;

    public List<Parser> parse(String analyzeText, List<List<String>> analysisResult, List<String> allVn, List<String> allVt, List<List<String[]>> vnInfer_separated) {
        this.analysisResult = analysisResult;
        this.analyzeText = analyzeText;
        this.allVn = allVn;
        this.allVt = allVt;
        VtInM = new ArrayList<>();
        VtInM.addAll(this.allVt);
        VtInM.remove("ε");
        VtInM.add("#");
        this.vnInfer_separated = vnInfer_separated;
        return getParseStep();
    }

    private List<Parser> getParseStep() {
        flag = true;
        stack = new Stack<>();
        init();
        String[] inputText = analyzeText.split("");
        int index = 0;
        String symbol = inputText[index];
        if (!VtInM.contains(symbol)) {
            flag = false;
            success = false;
        }
        String stackTop;
        while (flag) {
            stackTop = stack.pop();
            popParseProcess();
            if (allVt.contains(stackTop)) {
                if (stackTop.equals(symbol)) symbol = getNext(inputText, ++index);
                else {
                    error();
                    break;
                }
            } else if (stackTop.equals("#")) {
                if (stackTop.equals(symbol)) flag = false;
                else {
                    error();
                    break;
                }
            } else if (inMatrix(stackTop, symbol)) pushToStack(stackTop, symbol);
            else {
                error();
                break;
            }
        }
        return parseStep;
    }

    private void popParseProcess() {
        int length = parseProcess.length();
        int cut = 1;
        if (parseProcess.charAt(length - 1) == '\'') cut = 2;
        parseProcess = parseProcess.substring(0, length - cut);
    }

    private void init() {
        success = true;
        stack.push("#");
        stack.push(allVn.get(0));
        parseProcess = "#" + allVn.get(0);
        formula = "";
        movement = "INITIALIZE";
        parseStep.add(new Parser(step, parseProcess, analyzeText, formula, movement));
    }

    private void error() {
        step++;
        formula = "";
        movement = "ERROR";
        parseStep.add(new Parser(step, parseProcess, analyzeText, formula, movement));
        success = false;

    }

    private String getNext(String[] inputText, int index) {
        step++;
        String symbol = inputText[index];
        analyzeText = analyzeText.substring(1);
        formula = "";
        movement = "GET NEXT";
        parseStep.add(new Parser(step, parseProcess, analyzeText, formula, movement));
        if (!VtInM.contains(symbol)) {
            flag = false;
            success = false;
        }
        return symbol;
    }

    private boolean inMatrix(String stackTop, String symbol) {
        int indexStackTop = allVn.indexOf(stackTop) + 1;
        int indexSymbol = VtInM.indexOf(symbol) + 1;
        boolean exist = analysisResult.get(indexStackTop).get(indexSymbol).equals("Error");
        return !exist;
    }

    private void pushToStack(String stackTop, String symbol) {
        step++;
        int indexStackTop = allVn.indexOf(stackTop) + 1;
        int indexSymbol = VtInM.indexOf(symbol) + 1;
        formula = analysisResult.get(indexStackTop).get(indexSymbol);
        int startIndex = formula.indexOf(">");
        String inferString = formula.substring(startIndex + 1);
        if (inferString.equals("ε")) {
            movement = "POP";
            parseStep.add(new Parser(step, parseProcess, analyzeText, formula, movement));
        } else {
            char[] m = inferString.toCharArray();
            List<String> infer = new ArrayList<>();
            for (int index = 0; index < m.length; index++) {
                if (index < m.length - 1 && m[index + 1] == '\'') {
                    infer.add(m[index] + "" + m[index + 1]);
                    index++;
                } else
                    infer.add((m[index] + ""));
            }
            movement = "POP,PUSH(";
            for (int i = infer.size() - 1; i >= 0; i--) {
                stack.push(infer.get(i));
                parseProcess += infer.get(i);
                movement += infer.get(i);
            }
            movement += ")";
            parseStep.add(new Parser(step, parseProcess, analyzeText, formula, movement));
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
