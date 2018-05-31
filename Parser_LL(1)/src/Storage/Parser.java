package Storage;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Parser {

    private final SimpleIntegerProperty step;
    private final SimpleStringProperty stack;
    private final SimpleStringProperty remain;
    private final SimpleStringProperty formula;
    private final SimpleStringProperty movement;

    public Parser(int step, String stack, String remain, String formula, String movement){
        this.step = new SimpleIntegerProperty(step);
        this.stack = new SimpleStringProperty(stack);
        this.remain = new SimpleStringProperty(remain);
        this.formula =new SimpleStringProperty(formula);
        this.movement = new SimpleStringProperty(movement);
    }

    public int getStep(){
        return step.get();
    }

    public String getStack(){
        return stack.get();
    }

    public String getRemain(){
        return remain.get();
    }
    public String getFormula(){
        return formula.get();
    }
    public String getMovement(){
        return movement.get();
    }
}
