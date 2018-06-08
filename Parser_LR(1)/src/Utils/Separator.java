package Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Separator {

    public List<String> allVn;
    public List<String> allVt;
    public List<List<String[]>> vnInfer_separated;

    public void separate(List<String> vn, List<List<String>> vnInfer) {
        allVn = new ArrayList<>();
        allVt = new ArrayList<>();
        allVn.addAll(vn);
        vnInfer_separated = new ArrayList<>();

        for (List<String> everyVnInfer : vnInfer) {
            List<String[]> everyVnInfer_separated = new ArrayList<>();

            for (String singleInfer : everyVnInfer) {
                char[] chars = singleInfer.toCharArray();
                List<String> singleInfer_separated = new ArrayList<>();
                int length = chars.length;

                for (int index = 0; index < length; index++) {
                    String temp;
                    if (index < length - 1 && chars[index + 1] == '\'') {
                        temp = chars[index] + "" + chars[index + 1];
                        singleInfer_separated.add(temp);
                        index++;
                    } else
                        singleInfer_separated.add(temp = (chars[index] + ""));

                    if (!allVn.contains(temp))
                        allVt.add(temp);
                }
                String[] trans = new String[singleInfer_separated.size()];
                everyVnInfer_separated.add(singleInfer_separated.toArray(trans));
            }
            vnInfer_separated.add(everyVnInfer_separated);
        }

        allVt = allVt.stream().distinct().collect(Collectors.toList());     //去重
        allVn = allVn.stream().distinct().collect(Collectors.toList());
    }

}
