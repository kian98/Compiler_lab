package Storage;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ParseResult {
    private final SimpleIntegerProperty step;
    private final SimpleStringProperty status;
    private final SimpleStringProperty symbols;
    private final SimpleStringProperty remain;
    private final SimpleStringProperty movement;

    public ParseResult(int step, String status, String symbols, String remain, String movement){
        this.step = new SimpleIntegerProperty(step);
        this.status =new SimpleStringProperty(status);
        this.symbols = new SimpleStringProperty(symbols);
        this.remain = new SimpleStringProperty(remain);
        this.movement = new SimpleStringProperty(movement);
    }

    public int getStep(){
        return step.get();
    }
    public String getSymbols(){
        return symbols.get();
    }
    public String getRemain(){
        return remain.get();
    }
    public String getStatus(){
        return status.get();
    }
    public String getMovement(){
        return movement.get();
    }
}
