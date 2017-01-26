package classifier.test;

import classifier.controller.VocabularyBuilder;

/**
 * Created by Wijtse on 24-1-2017.
 */
public class TestVocaBuild {


    public static void main(String[] args) {
        VocabularyBuilder vb = new VocabularyBuilder("blogs");
        vb.loadWords();
        System.out.println("Loaded " + vb.getWordList().size() + " distinct words");
//        System.out.println(vb.getWordList());
        vb.cleanVocabulary(5, 5000);
        System.out.println("Word list contains " + vb.getWordList().size() + " distinct words after cleaning");

    }
}
