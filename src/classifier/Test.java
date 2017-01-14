package classifier;

import classifier.model.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        Word word = new Word("Woord");

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        List<Integer> list3 = new ArrayList<>();
        list1.add(10);
        list1.add(20);
        list1.add(30);
        list2.add(15);
        list2.add(5);
        list2.add(20);
        list3.add(25);
        list3.add(25);
        list3.add(50);
        Map<String, List<Integer>> M = new HashMap<>();
        M.put("M", list1);
        M.put("V", list2);
        M.put("T", list3);
        word.setM(M);

        List<String> keys = new ArrayList<>();
        keys.add("M");
        keys.add("V");
        keys.add("T");
        word.setKeys(keys);

        System.out.println(word);
    }
}
