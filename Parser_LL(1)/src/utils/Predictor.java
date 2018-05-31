package utils;

;
import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Predictor {

    public List<List<String>> analysisResult;

    public List<List<String>> predictTable(List<String> vtList, List<String> vnList, List<List<String[]>> vnInfers,
                                           List<List<String>> firstSet, List<List<String>> followSet) {
        analysisResult = new ArrayList<>();
        List<String> headRow = new ArrayList<>();
        headRow.add(" ");
        int countVt = 0;
        for (String vt : vtList) {
            headRow.add(vt);
            countVt++;
        }
        analysisResult.add(headRow);

        for (int i = 0; i < vnList.size(); i++) {
            List<String> eachRow = new ArrayList<>();
            eachRow.add(vnList.get(i));
            int col = 0;
            while (col < countVt) {
                eachRow.add("Error");
                col++;
            }
            List<String[]> allVnInfer = vnInfers.get(i);
            int location;
            for (String[] infer : allVnInfer) {
                String temp = StringUtils.join(Arrays.asList(infer), "");
                if (!vnList.contains(infer[0]) && !infer[0].equals("ε")) {
                    //如果为终结符或者空串(此处空串从vtlist中被remove),则直接添加到M[A,a]
                    location = indexInList(infer[0], vtList);
                    eachRow.set(location, vnList.get(i) + "->" + temp);
                } else {
                    //如果为非终结符,首先可以推导出first集中的终结符
                    location = indexInSet(infer[0], firstSet);
                    if (location > 0) {
                        for (String first : firstSet.get(location)) {
                            int index = indexInList(first, vtList);
                            if (vtList.contains(first)) eachRow.set(index, vnList.get(i) + "->" + temp);
                        }
                    } else {
                        location = indexInSet(vnList.get(i), followSet);
                        for (String follow : followSet.get(location)) {
                            int index = indexInList(follow, vtList);
                            if (vtList.contains(follow)) eachRow.set(index, vnList.get(i) + "->" + temp);
                        }
                    }
                }
            }

            analysisResult.add(eachRow);
        }

        return analysisResult;
    }

    private int indexInList(String target, List<String> list) {
        int index = 0;
        for (int i = 0; i < list.size(); i++)
            if (target.equals(list.get(i))) {
                index = i;
                break;
            }
        return index + 1;
    }

    private int indexInSet(String target, List<List<String>> set) {
        int index = 0;
        for (int i = 0; i < set.size(); i++)
            if (set.get(i).get(0).equals(target)) {
                index = i;
                break;
            }
        return index;
    }
}
