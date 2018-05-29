package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FirstSet {

    public List<List<String>> firstSet;

    public List<List<String>> getFirstSet(List<String> allVn, List<List<String[]>> vnInfer_separated) {
        firstSet = new ArrayList<>();
        for (String vn : allVn) {
            List<String> vnFirst = new ArrayList<>();       //对于某个Vn,它的first集内容
            vnFirst.add(vn);
            getFirst(vn, allVn, vnInfer_separated, vnFirst);
            vnFirst = vnFirst.stream().distinct().collect(Collectors.toList());
            firstSet.add(vnFirst);
        }
        return firstSet;
    }

    private List<String> getFirst(String alpha, List<String> allVn, List<List<String[]>> vnInfer_separated, List<String> vnFirst) {
        if (!allVn.contains(alpha)) {
            vnFirst.add(alpha);
            return vnFirst;
        }

        int index = allVn.indexOf(alpha);
        int count = 0;
        boolean nullFlag = false;
        for (String[] infer : vnInfer_separated.get(index)) {
            for (String singleInfer : infer) {
                List<String> new_vnFirst = new ArrayList<>();
                List<String> checkNull = getFirst(singleInfer, allVn, vnInfer_separated, new_vnFirst);
                vnFirst.addAll(checkNull);
                vnFirst.remove("ε");
                if (singleInfer.equals("ε")) nullFlag = true;       //自身可以推导出空串
                if (checkNull.contains("ε") && allVn.contains(singleInfer)) count++;  //判断推导内容全为非终结符
                else if (!checkNull.contains("ε")) break;           //若当前非终结符不包含空串,则不进行下一个非终结符的分析
            }
        }
        if (count == vnInfer_separated.size() || nullFlag) vnFirst.add("ε");     //推导内容全为非终结符且都推导出空串

        return vnFirst;
    }

}
