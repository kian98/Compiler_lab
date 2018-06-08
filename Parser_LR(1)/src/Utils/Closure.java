package Utils;

import Storage.Go;
import Storage.Item;

import java.util.ArrayList;
import java.util.List;

public class Closure {
    public List<List<Item>> closureList = new ArrayList<>();
    public List<Go> go = new ArrayList<>();
    private Separator separator;
    private List<List<String>> firstSet;
    private Grammars grammars;

    public void setClosure(Grammars grammarsIn,Separator separatorIn) {
        this.grammars = grammarsIn;
        this.separator = separatorIn;
        firstSet = FirstSet.getFirstSet(separator.allVn, separator.vnInfer_separated);
        List<String> symbolOfGrammar = new ArrayList<>();
        symbolOfGrammar.addAll(separator.allVn);
        symbolOfGrammar.addAll(separator.allVt);

        //初始化项目集族
        closureList.add(new ArrayList<>());
        closureList.get(0).add(new Item(grammars.vn.get(0), grammars.vn_infer.get(0).get(0), 0, "#"));
        createItemSet(closureList.get(0));
        for (int i = 0; i < closureList.size(); i++) {
            List<Item> itemList = closureList.get(i);
            for (String symbol : symbolOfGrammar) {
                List<Integer> itemToGo = goJudge(itemList, symbol);
                if (itemToGo.size() == 0) {
                    continue;
                }
                List<Item> newItemSet = new ArrayList<>();
                for (int index : itemToGo)
                    newItemSet.add(itemList.get(index).createNext());
                createItemSet(newItemSet);
                closureList.add(newItemSet);
                go.add(new Go(symbol, i, closureList.size() - 1));
            }
            simplify(closureList);
        }
        System.out.println(closureList);
    }

    private void createItemSet(List<Item> initSet) {
        //添加新项后会遍历到,若还能继续增加会继续添加新项
        int current = 0;
        while (current < initSet.size()) {
            Item item = initSet.get(current);
            if (item.index >= item.item_right.length()) {
                current++;
                continue;
            }
            String symbol = item.item_right.charAt(item.index) + "";
            if (!separator.allVn.contains(symbol)) {
                current++;
                continue;   //若圆点后一位为终结符.则不产生新的产生式
            }

            List<String> vtSet = new ArrayList<>();
            if (item.item_right.length() - item.index <= 1) {
                vtSet.addAll(item.forward);
            } else {      //若不相等,说明圆点后第二位有符号,则按照此符号产生展望符
                String beta = item.item_right.charAt(item.index + 1) + "";
                if (separator.allVt.contains(beta)) {
                    vtSet.add(beta);
                } else {
                    int j = separator.allVn.indexOf(symbol);
                    if (firstSet.get(j).contains("ε")) {
                        vtSet.addAll(item.forward);  //FIRST(βa),若β存在空串,则添加a
                    }
                    vtSet.addAll(firstSet.get(j).subList(1, firstSet.size()));
                    vtSet.remove("ε");
                }
            }

            int indexOfVn = grammars.vn.indexOf(symbol);
            for (String infer : grammars.vn_infer.get(indexOfVn)) {
                Item newItem = new Item(grammars.vn.get(indexOfVn), infer, 0, vtSet);
                boolean exsitInSet = false;
                for(Item i :initSet)
                    if(i.itemEquals(newItem))exsitInSet =true;
                if(!exsitInSet) initSet.add(newItem);
            }

            current++;
        }
    }

    private List<Integer> goJudge(List<Item> itemList, String X) {
        List<Integer> itemToGo = new ArrayList<>();
        for (Item item : itemList)
            if (item.index < item.item_right.length())
                if ((item.item_right.charAt(item.index) + "").equals(X)) itemToGo.add(itemList.indexOf(item));
        return itemToGo;
    }

    private void simplify(List<List<Item>> closureList) {
        List<List<Item>> original = new ArrayList<>(closureList);
        for (int i = 0; i < closureList.size(); i++) {
            for (int j = i + 1; j < closureList.size(); j++) {
                if (isSame(closureList.get(i), closureList.get(j))) {
                    int k =original.indexOf(closureList.get(j));
                    closureList.remove(j);
                    correctGo(i, k);
                }
            }
        }
    }

    private void correctGo(int i, int j) {
        for (Go eachGo : go) {
            if (eachGo.to == j)
                eachGo.to = i;
        }
    }

    private boolean isSame(List<Item> list1, List<Item> list2) {
        int count = 0;
        for (Item item1 : list1) {
            boolean exist = false;
            for (Item item2 : list2) {
                if (item1.itemEquals(item2)) exist = true;
            }
            if (exist) count++;
        }
        return count == list1.size();
    }
}
