package Storage;

import java.util.ArrayList;
import java.util.List;

public class Grammars {

    public List<String> vn;
    public List<List<String>> vn_infer;

    public Grammars(List<String> rawData) {
        vn = new ArrayList<>();
        vn_infer = new ArrayList<>();
        for (int i = 0; i < rawData.size(); i++) {
            String singleGrammar = rawData.get(i);
            vn.add(singleGrammar.charAt(0) + "");
            vn_infer.add(new ArrayList<>());
            for (String str : singleGrammar.substring(3).split("\\|"))
                vn_infer.get(i).add(str);
        }
    }

    public List<String> vnCopy() {
        return new ArrayList<>(vn);
    }

    public List<List<String>> vnInferCopy() {
        List<List<String>> vnInferCopy = new ArrayList<>();
        for (List<String> vnInferList : vn_infer) {
            List<String> list = new ArrayList<>(vnInferList);
            vnInferCopy.add(list);
        }
        return vnInferCopy;
    }
}
