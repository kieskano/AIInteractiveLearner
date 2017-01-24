package classifier;

import classifier.controller.VocabularyBuilder;

/**
 * Created by Wijtse on 24-1-2017.
 */
public class TestVocaBuild {


    public static void main(String[] args) {
        VocabularyBuilder vb = new VocabularyBuilder("testfiles");
        vb.loadWords();
        System.out.println("Loaded " + vb.getWordList().size() + " distinct words");
        System.out.println(vb.getWordList());
        System.out.println("Word list contains " + vb.getWordList().size() + " distinct words after cleaning");

    }
}
