package utils;

import Storage.Grammars;

import java.util.ArrayList;
import java.util.List;

public class EliminateLR {

    public String eliminate(Grammars grammar) {
        List<String> passedVT = new ArrayList<>();
        String leftR = "/*不包含左递归*/";

        boolean direct = false;
        for (int i = grammar.vt.size() - 1; i >= 0; i--) {
            int range = grammar.vt_infer.get(i).size();
            String vt_cur = grammar.vt.get(i);
            boolean flag = false;
            for (int j = 0; j < range; j++)
                if ((grammar.vt_infer.get(i).get(j).charAt(0) + "").equals(vt_cur)) flag = true;
            if (flag) {
                direct = true;
                directEliminate(grammar.vt, grammar.vt_infer, i, range, vt_cur);
            }
        }
        if (direct)
            leftR = "/*包含直接左递归*/";


        List<String> vtCopy = grammar.vtCopy();
        List<List<String>> vtInferCopy = grammar.vtInferCopy();

        boolean indirect = false;
        for (int i = vtCopy.size() - 1; i >= 0; i--) {
            String vt_cur = vtCopy.get(i);
            int range = vtInferCopy.get(i).size();
            for (int j = 0; j < range; j++) {
                String vtInfer = vtInferCopy.get(i).get(j);
                if (passedVT.contains(vtInfer.charAt(0) + "")) {
                    vtInferCopy.get(i).remove(vtInfer);
                    //range--;
                    j--;
                    List<String> temp = new ArrayList<>();
                    for (String str : vtInferCopy.get(vtCopy.indexOf(vtInfer.charAt(0) + "")))
                        temp.add(str + vtInfer.substring(1));
                    vtInferCopy.get(i).addAll(temp);
                    range = vtInferCopy.get(i).size();
                }
            }

            range = vtInferCopy.get(i).size();
            boolean flag = false;
            for (int j = 0; j < range; j++)
                if ((vtInferCopy.get(i).get(j).charAt(0) + "").equals(vt_cur)) flag = true;
            if (flag) {
                indirect = true;
                directEliminate(vtCopy, vtInferCopy, i, range, vt_cur);
            }
            passedVT.add(vt_cur);
        }
        if (indirect) {
            leftR = "/*包含间接左递归*/";
            grammar.vt = vtCopy;
            grammar.vt_infer = vtInferCopy;
            simplify(grammar);
        }

        return leftR;
    }

    private void directEliminate(List<String> vt, List<List<String>> vt_infers, int i, int range, String vt_cur) {
        vt.add(vt_cur + "\'");
        vt_infers.add(new ArrayList<>());
        for (int j = 0; j < range; ) {
            String vtInfer = vt_infers.get(i).get(j);
            vt_infers.get(i).remove(vtInfer);
            if ((vtInfer.charAt(0) + "").equals(vt_cur))
                vt_infers.get(vt.size() - 1).add(vtInfer.substring(1) + vt_cur + "\'");
            else
                vt_infers.get(i).add(vtInfer + vt_cur + "\'");
            range--;
        }
        vt_infers.get(vt.size() - 1).add("ε");
    }

    private void simplify(Grammars grammar) {
        List<String> toDeleteIndex = new ArrayList<>();
        for (String vtSearch : grammar.vt) {
            boolean contain = false;
            int toDelete;
            for (List<String> vtInferSearch : grammar.vt_infer)
                for (toDelete = 0; toDelete < vtInferSearch.size(); toDelete++)
                    if (vtInferSearch.get(toDelete).contains(vtSearch)) contain = true;
            if (!contain) toDeleteIndex.add(vtSearch);
        }
        for (String toDelete : toDeleteIndex) {
            int index = grammar.vt.indexOf(toDelete);
            if (index != 0) {
                grammar.vt.remove(index);
                grammar.vt_infer.remove(index);
            }
        }
    }
}
