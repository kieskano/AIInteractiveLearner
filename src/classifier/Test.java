package classifier;

import classifier.controller.FeatureSelect;
import classifier.controller.VocabularyBuilder;
import classifier.model.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        Word word1 = new Word("Woord nummero 1");
        List<Double> list11 = new ArrayList<>();
        List<Double> list12 = new ArrayList<>();
        List<Double> list13 = new ArrayList<>();
        list11.add(10.0);
        list11.add(20.0);
        list11.add(30.0);
        list12.add(15.0);
        list12.add(5.0);
        list12.add(20.0);
        list13.add(25.0);
        list13.add(25.0);
        list13.add(50.0);
        Map<String, List<Double>> M1 = new HashMap<>();
        M1.put("M", list11);
        M1.put("V", list12);
        M1.put("T", list13);
        word1.setM(M1);
        List<String> keys1 = new ArrayList<>();
        keys1.add("M");
        keys1.add("V");
        keys1.add("T");
        word1.setKeys(keys1);

        Word word2 = new Word("Woord nummero 2");
        List<Double> list21 = new ArrayList<>();
        List<Double> list22 = new ArrayList<>();
        List<Double> list23 = new ArrayList<>();
        List<Double> list24 = new ArrayList<>();
        list21.add(10.0);
        list21.add(20.0);
        list21.add(30.0);
        list22.add(15.0);
        list22.add(5.0);
        list22.add(20.0);
        list23.add(20.0);
        list23.add(15.0);
        list23.add(35.0);
        list24.add(45.0);
        list24.add(40.0);
        list24.add(85.0);
        Map<String, List<Double>> M2 = new HashMap<>();
        M2.put("M", list21);
        M2.put("V", list22);
        M2.put("N", list23);
        M2.put("T", list24);
        word2.setM(M2);
        List<String> keys2 = new ArrayList<>();
        keys2.add("M");
        keys2.add("V");
        keys2.add("N");
        keys2.add("T");
        word2.setKeys(keys2);

        word1.setE(FeatureSelect.getE(word1));
        word2.setE(FeatureSelect.getE(word2));
        word1.setChisq(FeatureSelect.getChisq(word1));
        word2.setChisq(FeatureSelect.getChisq(word2));

        System.out.println(word1);
        System.out.println(word2);

        VocabularyBuilder vocabularyBuilder = new VocabularyBuilder("blogs");
        vocabularyBuilder.loadWords();
        for (int i = 0; i < 10; i++) {
            System.out.println(vocabularyBuilder.getWordList().get(i));
        }

        for (Word word : vocabularyBuilder.getWordList()) {
            word.setE(FeatureSelect.getE(word));
            word.setChisq(FeatureSelect.getChisq(word));
        }
        System.out.println(FeatureSelect.getFeatures(vocabularyBuilder.getWordList(), 10));

    }
}
