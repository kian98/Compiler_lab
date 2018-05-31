package utils;

import Storage.Grammars;

import java.util.ArrayList;
import java.util.List;

public class EliminateLR {

    public String eliminate(Grammars grammar) {
        List<String> passedVT = new ArrayList<>();
        String leftR = "/*不包含左递归*/";

        boolean direct = false;     //是否为直接左递归
        for (int i = grammar.vn.size() - 1; i >= 0; i--) {
            int range = grammar.vn_infer.get(i).size();
            String vt_cur = grammar.vn.get(i);
            boolean flag = false;
            for (int j = 0; j < range; j++)         //若当前非终结符与对应各个导出式的首个标识符相同,说明为直接左递归
                if ((grammar.vn_infer.get(i).get(j).charAt(0) + "").equals(vt_cur)) flag = true;
            if (flag) {
                direct = true;
                directEliminate(grammar.vn, grammar.vn_infer, i, range, vt_cur);        //消除直接左递归
            }
        }
        if (direct)
            leftR = "/*包含直接左递归*/";

//      分别复制非终结符集合与对应导出式集合,进行间接左递归操作与判断
//      在一次遍历后,已完成左递归消除,若不为间接左递归,则不保存结果
        List<String> vtCopy = grammar.vnCopy();
        List<List<String>> vnInferCopy = grammar.vnInferCopy();

        boolean indirect = false;
        for (int i = vtCopy.size() - 1; i >= 0; i--) {      //倒序遍历,因为输入文法一般为由上而下书写顺序
            String vn_cur = vtCopy.get(i);
            int range = vnInferCopy.get(i).size();          //获取每一个非终结符对应导出式个数
            for (int j = 0; j < range; j++) {
                String vnInfer = vnInferCopy.get(i).get(j);     //获取非终结符导出式
                if (passedVT.contains(vnInfer.charAt(0) + "")) {        //已处理左递归的非终结符中是否包含当前字符
                    vnInferCopy.get(i).remove(vnInfer);
                    j--;
                    List<String> temp = new ArrayList<>();
                    for (String str : vnInferCopy.get(vtCopy.indexOf(vnInfer.charAt(0) + "")))
                        temp.add(str + vnInfer.substring(1));
                    vnInferCopy.get(i).addAll(temp);        //代入非终结符直至非终结符起始
                    range = vnInferCopy.get(i).size();
                }
            }

            range = vnInferCopy.get(i).size();
            boolean flag = false;
            for (int j = 0; j < range; j++)
                if ((vnInferCopy.get(i).get(j).charAt(0) + "").equals(vn_cur)) flag = true;
            if (flag) {
                indirect = true;
                directEliminate(vtCopy, vnInferCopy, i, range, vn_cur);
            }
            passedVT.add(vn_cur);
        }
        if (indirect) {
            leftR = "/*包含间接左递归*/";
            grammar.vn = vtCopy;
            grammar.vn_infer = vnInferCopy;
            simplify(grammar);
        }

        return leftR;
    }

//      消除直接左递归:
//      将 P->Pα1|Pα2|...|Pαm|β1|β2|...|βm 转变为
//      P->β1P'|β2P'|...|βmP'  ,  P'->α1P'|α2P'|...|αmP'|ε
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
