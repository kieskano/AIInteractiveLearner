package classifier;

import classifier.controller.FeatureSelect;
import classifier.controller.VocabularyBuilder;
import classifier.model.Word;

public class FeatureTest {

    public static void main(String[] args) {
        VocabularyBuilder vocabularyBuilder = new VocabularyBuilder("blogs");
        vocabularyBuilder.loadWords();
        System.out.println(vocabularyBuilder.getWordList().size());
        vocabularyBuilder.cleanVocabulary(6, 1000);
        System.out.println(vocabularyBuilder.getWordList().size());
        //for (int i = 0; i < 10; i++) {
        //    System.out.println(vocabularyBuilder.getWordList().get(i));
        //}

        for (Word word : vocabularyBuilder.getWordList()) {
            word.setE(FeatureSelect.getE(word));
            word.setChisq(FeatureSelect.getChisq(word));
        }
        System.out.println(FeatureSelect.getFeatures(vocabularyBuilder.getWordList(), 10, "H"));
        System.out.println(FeatureSelect.getFeaturesNaive(vocabularyBuilder.getWordList(), 10));
    }



}
