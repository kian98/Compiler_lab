package utils;

import Storage.Grammars;

import java.util.ArrayList;
import java.util.List;

public class EliminateLR {

    public String eliminate(Grammars grammar) {
        List<String> passedVT = new ArrayList<>();
        String leftR = "/*不包含左递归*/";

        boolean direct = false;
        for (int i = grammar.vn.size() - 1; i >= 0; i--) {
            int range = grammar.vn_infer.get(i).size();
            String vt_cur = grammar.vn.get(i);
            boolean flag = false;
            for (int j = 0; j < range; j++)
                if ((grammar.vn_infer.get(i).get(j).charAt(0) + "").equals(vt_cur)) flag = true;
            if (flag) {
                direct = true;
                directEliminate(grammar.vn, grammar.vn_infer, i, range, vt_cur);
            }
        }
        if (direct)
            leftR = "/*包含直接左递归*/";


        List<String> vtCopy = grammar.vnCopy();
        List<List<String>> vnInferCopy = grammar.vnInferCopy();

        boolean indirect = false;
        for (int i = vtCopy.size() - 1; i >= 0; i--) {
            String vt_cur = vtCopy.get(i);
            int range = vnInferCopy.get(i).size();
            for (int j = 0; j < range; j++) {
                String vnInfer = vnInferCopy.get(i).get(j);
                if (passedVT.contains(vnInfer.charAt(0) + "")) {
                    vnInferCopy.get(i).remove(vnInfer);
                    //range--;
                    j--;
                    List<String> temp = new ArrayList<>();
                    for (String str : vnInferCopy.get(vtCopy.indexOf(vnInfer.charAt(0) + "")))
                        temp.add(str + vnInfer.substring(1));
                    vnInferCopy.get(i).addAll(temp);
                    range = vnInferCopy.get(i).size();
                }
            }

            range = vnInferCopy.get(i).size();
            boolean flag = false;
            for (int j = 0; j < range; j++)
                if ((vnInferCopy.get(i).get(j).charAt(0) + "").equals(vt_cur)) flag = true;
            if (flag) {
                indirect = true;
                directEliminate(vtCopy, vnInferCopy, i, range, vt_cur);
            }
            passedVT.add(vt_cur);
        }
        if (indirect) {
            leftR = "/*包含间接左递归*/";
            grammar.vn = vtCopy;
            grammar.vn_infer = vnInferCopy;
            simplify(grammar);
        }

        return leftR;
    }

    private void directEliminate(List<String> vt, List<List<String>> vt_infers, int i, int range, String vt_cur) {
        vt.add(vt_cur + "\'");
        vt_infers.add(new ArrayList<>());
        for (int j = 0; j < range; ) {
            String vnInfer = vt_infers.get(i).get(j);
            vt_infers.get(i).remove(vnInfer);
            if ((vnInfer.charAt(0) + "").equals(vt_cur))
                vt_infers.get(vt.size() - 1).add(vnInfer.substring(1) + vt_cur + "\'");
            else
                vt_infers.get(i).add(vnInfer + vt_cur + "\'");
            range--;
        }
        vt_infers.get(vt.size() - 1).add("ε");
    }

    private void simplify(Grammars grammar) {
        List<String> toDeleteIndex = new ArrayList<>();
        for (String vtSearch : grammar.vn) {
            boolean contain = false;
            int toDelete;
            for (List<String> vnInferSearch : grammar.vn_infer)
                for (toDelete = 0; toDelete < vnInferSearch.size(); toDelete++)
                    if (vnInferSearch.get(toDelete).contains(vtSearch)) contain = true;
            if (!contain) toDeleteIndex.add(vtSearch);
        }
        for (String toDelete : toDeleteIndex) {
            int index = grammar.vn.indexOf(toDelete);
            if (index != 0) {
                grammar.vn.remove(index);
                grammar.vn_infer.remove(index);
            }
        }
    }
}
