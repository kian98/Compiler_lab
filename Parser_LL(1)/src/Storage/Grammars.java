package Storage;

import java.util.ArrayList;
import java.util.List;

public class Grammars {

    public List<String> vn;
    public List<List<String>> vn_infer;

//    默认读入格式为"X->**|**|*".
//    Grammars方法传入包含多条String类型的文法,每条文法首个字母即为初始非终结符.
//    并将每条文法的导出式按分隔符"|"分割,保存为List<String>类型.
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

//    返回一个非终结符List的复制
    public List<String> vnCopy() {
        return new ArrayList<>(vn);
    }

//    返回一个非终结符对应推导的复制
    public List<List<String>> vnInferCopy() {
        List<List<String>> vnInferCopy = new ArrayList<>();
        for (List<String> vnInferList : vn_infer) {
            List<String> list = new ArrayList<>(vnInferList);
            vnInferCopy.add(list);
        }
        return vnInferCopy;
    }
}
