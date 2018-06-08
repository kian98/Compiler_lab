package Models;

public class Go {
    private String symbol;
    private int start;
    private int to;

    public Go(String symbol, int start, int to) {
        this.symbol = symbol;
        this.start = start;
        this.to = to;
    }

    public int getStart() {
        return start;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getTo() {
        return to;
    }
}
