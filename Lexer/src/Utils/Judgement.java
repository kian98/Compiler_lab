package Utils;

public class Judgement {
    private String[] separators;
    private String[] arithmeticOperators;
    private String[] relationOperators;
    private String[] keywords;
    private String[] type;

    public Judgement() {
        keywords = new String[]{"include", "do", "end", "for", "if", "printf", "scanf", "then", "while"};
        relationOperators = new String[]{"<", "<=", "=", ">", ">=", "<>"};
        arithmeticOperators = new String[]{"+", "-", "*", "/", "++", "--"};
        separators = new String[]{",", ";", "(", ")", "{", "}", "\"", "#"};
        type = new String[]{"关键词", "分界符", "算术运算符", "关系运算符", "常数", "标识符", "ERROR"};
    }

    public boolean addNewValue(int index, String newValue) {
        String[] valuesTemp;
        switch (index) {
            case 0:
                if (!isKeyword(newValue)) {
                    valuesTemp = new String[keywords.length + 1];
                    System.arraycopy(keywords, 0, valuesTemp, 0, keywords.length);
                    valuesTemp[keywords.length] = newValue;
                    keywords = valuesTemp;
                    return true;
                }
                break;
            case 1:
                if (!isSeparator(newValue)) {
                    valuesTemp = new String[separators.length + 1];
                    System.arraycopy(separators, 0, valuesTemp, 0, separators.length);
                    valuesTemp[separators.length] = newValue;
                    separators = valuesTemp;
                    return true;
                }
                break;
            case 2:
                if (!isRelationOperator(newValue)) {
                    valuesTemp = new String[relationOperators.length + 1];
                    System.arraycopy(relationOperators, 0, valuesTemp, 0, relationOperators.length);
                    valuesTemp[relationOperators.length] = newValue;
                    relationOperators = valuesTemp;
                    return true;
                }
                break;
            case 3:
                if (!isArithmeticOperator(newValue)) {
                    valuesTemp = new String[arithmeticOperators.length + 1];
                    System.arraycopy(arithmeticOperators, 0, valuesTemp, 0, arithmeticOperators.length);
                    valuesTemp[arithmeticOperators.length] = newValue;
                    arithmeticOperators = valuesTemp;
                    return true;
                }
                break;
        }
        return false;
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
