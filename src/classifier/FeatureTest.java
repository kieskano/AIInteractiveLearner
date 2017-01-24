package classifier;

import classifier.controller.FeatureSelect;
import classifier.controller.VocabularyBuilder;
import classifier.model.Word;

public class FeatureTest {

    public static void main(String[] args) {
        VocabularyBuilder vocabularyBuilder = new VocabularyBuilder("testfiles");
        vocabularyBuilder.loadWords();
        vocabularyBuilder.cleanVocabulary(10, 3500);
        System.out.println(vocabularyBuilder.getWordList().size());
        //for (int i = 0; i < 10; i++) {
        //    System.out.println(vocabularyBuilder.getWordList().get(i));
        //}

        for (Word word : vocabularyBuilder.getWordList()) {
            word.setE(FeatureSelect.getE(word));
            word.setChisq(FeatureSelect.getChisq(word));
        }
        System.out.println(FeatureSelect.getFeatures(vocabularyBuilder.getWordList(), 10, "F"));
        System.out.println(FeatureSelect.getFeaturesNaive(vocabularyBuilder.getWordList(), 10));
    }



}
