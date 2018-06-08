package Models;

import java.util.ArrayList;
import java.util.List;

public class Item {
    public String item_left;
    public String item_right;
    public List<String> forward = new ArrayList<>();
    public int index;

    public Item(String item_left, String item_right, int index, String forward) {
        this.item_left = item_left;
        this.item_right = item_right;
        this.index = index;
        this.forward.add(forward);
    }

    public Item(String item_left, String item_right, int index, List<String> forward) {
        this.item_left = item_left;
        this.item_right = item_right;
        this.index = index;
        this.forward.addAll(forward);
    }

    public Item createNext() {
        return new Item(item_left, item_right, index + 1, forward);
    }


    public boolean itemEquals(Item obj) {
        if (this.index != obj.index) return false;
        if (!this.item_left.equals(obj.item_left)) return false;
        if (!this.item_right.equals(obj.item_right)) return false;
        if (!this.forward.equals(obj.forward))return false;
        return true;
    }
}
