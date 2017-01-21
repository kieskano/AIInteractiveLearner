package classifier.controller;

import classifier.model.Word;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by han on 21-1-17.
 */
public class NaiveBayesianClassifier {

    private static VocabularyBuilder vocabularyBuilder;
    private static Classifier classifier;
    //private static Updater updater;

    private static Map<String, Word> vocab;
    private static Map<String, Word> cleanVocab;
    private static Map<String, Word> features;

    private static String directory;
    private static int amountOfFeatures;
    private static int smoothingConstant;
    private static int minFreq;
    private static int maxFreq;

    public static void main(String[] args) {
        //Parse args
        directory = args[0];
        amountOfFeatures = Integer.parseInt(args[1]);
        smoothingConstant = Integer.parseInt(args[2]);
        minFreq = Integer.parseInt(args[3]);
        maxFreq = Integer.parseInt(args[4]);

        //Initialization
        //TODO: Reset training data

        vocabularyBuilder = new VocabularyBuilder(directory);
        classifier = new Classifier();
        //updater = new Updater();

        vocab = new HashMap<>();
        cleanVocab = new HashMap<>();
        features = new HashMap<>();

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
