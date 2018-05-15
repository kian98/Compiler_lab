package Storager;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

//用于存储每一个Token的信息
//并与TableView关联，以实现输出
public class LexResult {
    class binarySeq {
        SimpleIntegerProperty wordTypeRank;
        SimpleStringProperty wordProperty;
    }

    class location {
        SimpleIntegerProperty row;
        SimpleIntegerProperty line;
    }

    private final SimpleStringProperty word;
    private final binarySeq biSeq = new binarySeq();
    private final SimpleStringProperty type;
    private final location location = new location();

    public LexResult(String word, int rank, String type, int row, int line) {
        this.word = new SimpleStringProperty(word);
        this.biSeq.wordTypeRank = new SimpleIntegerProperty(rank);
        this.biSeq.wordProperty = (rank != 7 ? this.word : new SimpleStringProperty("ERROR"));
        this.type = new SimpleStringProperty(type);
        this.location.row = new SimpleIntegerProperty(row);
        this.location.line = new SimpleIntegerProperty(line);
    }

    public String getWord() {
        return word.get();
    }

    public void setWord(String word) {
        this.word.set(word);
    }

    public int getWordTypeRank() {
        return biSeq.wordTypeRank.get();
    }

    public void setWordTypeRank(int rank) {
        this.biSeq.wordTypeRank.set(rank);
    }

    public String getWordProperty() {
        return biSeq.wordProperty.get();
    }

    public void setWordProperty(String property) {
        this.biSeq.wordProperty.set(property);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getLine() {
        return location.row.get();
    }

    public void setLine(int line) {
        this.location.row.set(line);
    }

    public int getRow() {
        return location.line.get();
    }

    public void setRow(int row) {
        this.location.line.set(row);
    }
}
