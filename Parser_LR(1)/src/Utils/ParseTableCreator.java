package Utils;

import Storage.Go;
import Storage.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParseTableCreator {
    public List<String[]> ACTION = new ArrayList<>();
    public List<String[]> GOTO = new ArrayList<>();

    public void createParseTable(List<List<Item>> closureList, List<Go> go, List<String> vn, List<String> vt, List<List<String>> extendedGrammar) {
        init(closureList.size(), vn, vt);
        for (int itemIndex = 0; itemIndex < closureList.size(); itemIndex++) {
            List<Item> itemList = closureList.get(itemIndex);
            for (Item item : itemList) {
                if (item.index == item.item_right.length()) {
                    if (item.item_left.equals("S'")) {
                        ACTION.get(itemIndex+1)[vt.size()] = "acc";
                    } else if (inVt(item.forward, vt)) {
                        int indexIj = extendedGrammar.get(1).indexOf(item.item_right);
                        for (String str : item.forward)
                            ACTION.get(itemIndex+1)[vt.indexOf(str)] = "r" + indexIj;
                    } else if (item.forward.contains("#")) {
                        int indexIj = extendedGrammar.get(1).indexOf(item.item_right);
                        ACTION.get(itemIndex+1)[vt.size()] = "r" + indexIj;
                    }
                } else {
                    String behindDot = item.item_right.charAt(item.index) + "";
                    int nextItem;
                    if (vt.contains(behindDot) && (nextItem = exsitGo(itemIndex, behindDot, go)) >= 0)
                        ACTION.get(itemIndex+1)[vt.indexOf(behindDot)] = "s" + nextItem;
                    else if (vn.contains(behindDot) && (nextItem = exsitGo(itemIndex, behindDot, go)) >= 0)
                        GOTO.get(itemIndex+1)[vn.indexOf(behindDot)] = nextItem + "";
                }
            }
        }
    }

    private void init(int itemCount, List<String> vn, List<String> vt) {
        for (int i = 0; i < itemCount + 1; i++) { //第一行为标题,显示所有终结符与非终结符(包括#)
            String actionOfItem[] = new String[vt.size() + 1];
            Arrays.fill(actionOfItem, " ");
            String gotoOfItem[] = new String[vn.size()];
            Arrays.fill(gotoOfItem, " ");
            ACTION.add(actionOfItem);
            GOTO.add(gotoOfItem);
        }
        for (int j = 0; j < vt.size(); j++)
            ACTION.get(0)[j] = vt.get(j);
        ACTION.get(0)[vt.size()] = "#";
        for (int k = 0; k < vn.size(); k++)
            GOTO.get(0)[k] = vn.get(k);
    }

    private int exsitGo(int itemIndex, String behindDot, List<Go> go) {
        int nextItem = -1;
        for (Go eachGo : go) {
            if (eachGo.start == itemIndex && eachGo.symbol.equals(behindDot))
                nextItem = eachGo.to;
        }
        return nextItem;
    }

    private boolean inVt(List<String> forward, List<String> vt) {
        for (String str : forward) if (!vt.contains(str)) return false;
        return true;
    }


}
