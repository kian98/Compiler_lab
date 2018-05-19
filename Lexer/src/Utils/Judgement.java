package Utils;

public class Judgement {
    private String[] separators;
    private String[] arithmeticOperators;
    private String[] relationOperators;
    private String[] keywords;
    private String[] type;

    Judgement() {
        keywords = new String[]{"do", "end", "for", "if", "printf", "scanf", "then", "while"};
        relationOperators = new String[]{"<", "<=", "=", ">", ">=", "<>"};
        arithmeticOperators = new String[]{"+", "-", "*", "/", "++", "--"};
        separators = new String[]{",", ";", "(", ")", "{", "}", "\"","#"};
        type = new String[]{"关键字", "分界符", "算术运算符", "关系运算符", "常数", "标识符", "ERROR"};
    }

    public boolean isKeyword(String str) {
        for (String keyWord : keywords) {
            if (str.equals(keyWord))
                return true;
        }
        return false;
    }

    public boolean isSeparator(String str) {
        for (String separator : separators) {
            if (str.equals(separator))
                return true;
        }
        return false;
    }

    public boolean isArithmeticOperator(String str) {
        for (String arithmeticOperator : arithmeticOperators) {
            if (str.equals(arithmeticOperator))
                return true;
        }
        return false;
    }

    public boolean isRelationOperator(String str) {
        for (String relationOperator : relationOperators) {
            if (str.equals(relationOperator))
                return true;
        }
        return false;
    }

    public boolean isOperator(String str) {
        return isArithmeticOperator(str) || isRelationOperator(str);
    }

    public boolean isBlank(char ch) {
        return ch == (' ');
    }

    public String getType(int wordType) {
        return type[wordType - 1];
    }
}
