package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Separator {

    public void separate(List<String> vt, List<List<String>> vtInfer) {
        List<String> allVt = new ArrayList<>();
        List<String> allVn = new ArrayList<>();
        allVt.addAll(vt);

        List<List<String[]>> vtInfer_separated = new ArrayList<>();
        for (List<String> everyVtInfer : vtInfer) {
            List<String[]> everyVtInfer_separated = new ArrayList<>();

            for (String singleInfer : everyVtInfer) {
                char[] chars = singleInfer.toCharArray();
                List<String> singleInfer_separated = new ArrayList<>();
                int length = chars.length;

                for (int index = 0; index < length; index++) {
                    String temp;
                    if (index < length - 1 && chars[index + 1] == '\'') {
                        temp = chars[index] + "" + chars[index + 1];
                        singleInfer_separated.add(temp);
                        index++;
                    }else
                        singleInfer_separated.add(temp=(chars[index]+""));

                    if(!allVt.contains(temp))
                        allVn.add(temp);
                }
                String[] trans = new String[singleInfer_separated.size()];
                everyVtInfer_separated.add(singleInfer_separated.toArray(trans));
            }
            vtInfer_separated.add(everyVtInfer_separated);
        }

        allVn = allVn.stream().distinct().collect(Collectors.toList());
        allVt = allVt.stream().distinct().collect(Collectors.toList());
    }

}
