package Storage;

import java.util.ArrayList;
import java.util.List;

public class Grammars {

    public List<String> vt;
    public List<List<String>> vt_infer;

    public Grammars(List<String> rawData) {
        vt = new ArrayList<>();
        vt_infer = new ArrayList<>();
        for (int i = 0; i < rawData.size(); i++) {
            String singleGrammar = rawData.get(i);
            vt.add(singleGrammar.charAt(0) + "");
            vt_infer.add(new ArrayList<>());
            for (String str : singleGrammar.substring(3).split("\\|"))
                vt_infer.get(i).add(str);
        }
    }

    public List<String> vtCopy() {
        return new ArrayList<>(vt);
    }

    public List<List<String>> vtInferCopy() {
        List<List<String>> vtInferCopy = new ArrayList<>();
        for (List<String> vtInferList : vt_infer) {
            List<String> list = new ArrayList<>(vtInferList);
            vtInferCopy.add(list);
        }
        return vtInferCopy;
    }
}
