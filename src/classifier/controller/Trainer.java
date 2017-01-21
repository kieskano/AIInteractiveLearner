package classifier.controller;

import classifier.model.Word;

import java.util.HashMap;
import java.util.Map;

public class Trainer {

    private Map<String, Word> vocab;
    private Map<String, Word> cleanVocab;
    private Map<String, Word> features;

    public void train(String directory) {
        vocab = new HashMap<>();
        cleanVocab = new HashMap<>();
        features = new HashMap<>();

        VocabularyBuilder vocabularyBuilder = new VocabularyBuilder(directory)

        //Fill vocab lists
        vocabularyBuilder.loadWords();
        for (Word word : vocabularyBuilder.getWordList()) {
            vocab.put(word.getWord(), word);
        }
        vocabularyBuilder.cleanVocabulary(minFreq, maxFreq);
        for (Word word : vocabularyBuilder.getWordList()) {
            cleanVocab.put(word.getWord(), word);
        }
    }



}
