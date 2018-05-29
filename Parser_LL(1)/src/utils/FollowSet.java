package utils;

import javax.swing.text.html.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FollowSet {

    public List<List<String>> followSet;
    List<String> allVn_Local;
    List<List<String[]>> vnInfer_separated_Local;
    List<List<String>> firstSet_Local;

    public List<List<String>> getFollowSet(List<String> allVn, List<List<String[]>> vnInfer_separated, List<List<String>> firstSet) {
        allVn_Local = allVn;
        vnInfer_separated_Local = vnInfer_separated;
        firstSet_Local = firstSet;

        followSet = new ArrayList<>();
        for (String vn : allVn) {
            List<String> vnFollow = new ArrayList<>();
            vnFollow.add(vn);
            getFollow(vn, vnFollow);
            vnFollow = vnFollow.stream().distinct().collect(Collectors.toList());
            followSet.add(vnFollow);
        }
        return followSet;
    }

    private List<String> getFollow(String vn, List<String> vnFollow) {
        if (vn.equals(allVn_Local.get(0))) vnFollow.add("#");

        for (int inferListIndex = 0; inferListIndex < vnInfer_separated_Local.size(); inferListIndex++) {
            for (String[] infer : vnInfer_separated_Local.get(inferListIndex)) {
                int index = getIndex(vn, infer);
                if (index < 0) continue;
                List<String> betaFirst = new ArrayList<>();
                if (index + 1 < infer.length) {       //A->αBβ
                    String beta = infer[index + 1];
                    if (allVn_Local.contains(beta)) {
                        int betaIndex = allVn_Local.indexOf(beta);
                        betaFirst = firstSet_Local.get(betaIndex);
                        vnFollow.addAll(betaFirst.subList(1, betaFirst.size()));
                        vnFollow.remove("ε");
                    } else
                        vnFollow.add(beta);
                }

                if (index + 1 >= infer.length || betaFirst.contains("ε")) {
                    List<String> new_aFollow = new ArrayList<>();
                    if (allVn_Local.get(inferListIndex).equals(vn))
                        continue;       //如果下一次递归所求follow集与当前所求的为同一Vn的，说明迭代结果不变，不需要继续
                    new_aFollow.add(allVn_Local.get(inferListIndex));
                    List<String> aFollow = getFollow(allVn_Local.get(inferListIndex), new_aFollow);
                    vnFollow.addAll(aFollow.subList(1, aFollow.size()));
                }
            }
        }
        return vnFollow;
    }


    private int getIndex(String str, String[] strings) {
        int index = -1;
        for (int i = 0; i < strings.length; i++)
            if (str.equals(strings[i])) {
                index = i;
                break;
            }
        return index;
    }

}
