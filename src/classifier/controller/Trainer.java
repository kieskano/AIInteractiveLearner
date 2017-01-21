package classifier.controller;

import classifier.model.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trainer {

    private Map<String, Word> vocab;
    private Map<String, Word> cleanVocab;
    private Map<String, Word> features;

    public void train() {
        System.out.println("|-- Starting training");

        vocab = new HashMap<>();
        cleanVocab = new HashMap<>();
        features = new HashMap<>();

        VocabularyBuilder vocabularyBuilder = new VocabularyBuilder(NaiveBayesianClassifier.getDirectory());

        //Fill vocab lists
        System.out.println("|-- Filling vocabulary lists...");

        vocabularyBuilder.loadWords();

        for (Word word : vocabularyBuilder.getWordList()) {
            vocab.put(word.getWord(), word);
        }

        vocabularyBuilder.cleanVocabulary(NaiveBayesianClassifier.getMinFreq(), NaiveBayesianClassifier.getMaxFreq());

        for (Word word : vocabularyBuilder.getWordList()) {
            cleanVocab.put(word.getWord(), word);
        }

        for(Word word : vocabularyBuilder.getWordList()) {
            word.setE(FeatureSelect.getE(word));
            word.setChisq(FeatureSelect.getChisq(word));
        }
        List<Word> featureList = new ArrayList<>();
        for (String c : vocabularyBuilder.getClasses()) {
            featureList.addAll(FeatureSelect.getFeatures(vocabularyBuilder.getWordList(),
                    NaiveBayesianClassifier.getAmountOfFeatures(), c));
        }

        for (Word word : featureList) {
            features.put(word.getWord(), word);
        }

        System.out.println("|-- Vocabulary lists filled.");
    }



}
