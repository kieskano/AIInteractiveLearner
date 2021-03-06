package classifier.controller;


import classifier.model.Word;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Classifier {

    public static final String TEST_DIRECTORY_NAME = "test";

    private List<String> words;
    private List<String> documentFeatures;
    private Map<String, Integer> totalWordCount;
    private Map<String, Map<String, Double>> chances;
    private Map<String, Integer> wordCountPerDocument;

    public static final int MIN_FREQ_MOD = 3;

    /**
     * Classifies the given file.
     * @param filename Name of the file to be classified
     * @return Name of the class the file has been classified as
     */
    public String classify(String filename) {
        String fileLocation = NaiveBayesianClassifier.getDirectory() + File.separator + TEST_DIRECTORY_NAME
                + File.separator + filename;
        File unclassifiedFile = new File(fileLocation);

        String result = null;
        if (unclassifiedFile.exists() && unclassifiedFile.isFile()) {
            result = calculate(unclassifiedFile);
        }
        return result;
    }

    /**
     * Classifies the given file.
     * @param file file to be classified
     * @return Name of the class the file has been classified as
     */
    public String classify(File file) {
        return calculate(file);
    }

    /**
     * Classifies the given file.
     * @param unclassifiedFile file to be classified
     * @return Name of the class the file has been classified as
     */
    public String calculate(File unclassifiedFile) {
        words = new ArrayList<>();
        documentFeatures = new ArrayList<>();
        chances = new HashMap<>();
        totalWordCount = new HashMap<>();
        wordCountPerDocument = new HashMap<>();

        System.out.println("-|- Started classification");
        String result = "";



        System.out.println("-|- Reading file...");
        List<String> lines = new ArrayList<>();
        try {
            try {
                lines = Files.readAllLines(unclassifiedFile.toPath(), Charset.defaultCharset());
            } catch (IOException e) {
                System.out.println("-|- Could not read " + unclassifiedFile.getName() + " with default charset. Trying different charsets...");
                for (Charset charset : Charset.availableCharsets().values()) {
                    try {
                        System.out.println("-|- Trying " + charset.toString());
                        lines = Files.readAllLines(unclassifiedFile.toPath(), charset);
                        System.out.println("-|- Read " + unclassifiedFile.getName() + " with " + charset.toString());
                        break;
                    } catch (IOException ex) {
                        //
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("-|- Error reading file, will now exit");
            System.exit(1);
        }
        System.out.println("-|- File read");


        System.out.println("-|- Normalizing words...");
        for (String line : lines) {
            String[] wordArray = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
            words.addAll(Arrays.asList(wordArray));
        }
        System.out.println("-|- Words normalized");


        System.out.println("-|- Cleaning document vocabulary...");
        int minFreq = NaiveBayesianClassifier.getMinFreq() / MIN_FREQ_MOD;
        int maxFreq = (int) (NaiveBayesianClassifier.getMaxFreq() /
                NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getTotalNrOfFiles());
        words = cleanVocabulary(words, minFreq, maxFreq);
        System.out.println("-|- Vocabulary cleaned");


        System.out.println("-|- Calculating chances for words given a class...");
        Map<String, Word> features = NaiveBayesianClassifier.getTrainer().getFeatures();
        for (String c : NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getClasses()) {
            int totalWordCountClass = 0;
            for (Word word : features.values()) {
                totalWordCountClass += word.getOccurrencesPerClass().get(c);
            }
            totalWordCount.put(c, totalWordCountClass);
        }

        for (String word : words) {
            if (!wordCountPerDocument.containsKey(word)) {
                Map<String, Double> chancesPerClass = new HashMap<>();
                wordCountPerDocument.put(word, 1);
                for (String c : NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getClasses()) {
                    int occurrencesPerClass = 0;
                    if (features.containsKey(word)) {
                        Word currentFeature = features.get(word);
                        occurrencesPerClass = currentFeature.getOccurrencesPerClass().get(c);
                    }
                    double smoothing = NaiveBayesianClassifier.getSmoothingConstant();
                    double chance = (occurrencesPerClass + smoothing) /
                            (totalWordCount.get(c) + (smoothing * features.size()));
                    chancesPerClass.put(c, chance);
                }
                chances.put(word, chancesPerClass);
            } else {
                wordCountPerDocument.replace(word, wordCountPerDocument.get(word) + 1);
            }
        }

        //axbxc = loga + logb + logc
        // D1: a a a b
        // P(C) * P(a)³ * P(b) = logPc + logPa + logPa + logPa + logPb

        System.out.println("-|- Calculating chances for class given the new document...");
        double currentBestChance = 0;
        Map<String, Double> numberOfFiles =
                NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getNumberOfFiles();
        double totalNrOfFiles = NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getTotalNrOfFiles();
        for (String c : NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getClasses()) {
            double numberOfFilesPerClass = numberOfFiles.get(c);
            double chanceOfClass = numberOfFilesPerClass / totalNrOfFiles;
            double currentChance = log2(chanceOfClass);
            for (String word : wordCountPerDocument.keySet()) {
                for (int i = 0; i < wordCountPerDocument.get(word); i++) {
                    currentChance += log2(chances.get(word).get(c));
                }
            }
            System.out.println("-|- " + c + " " + currentChance);
            //log2(0.001) = -9 log2(0.0001) = -13 so the higher chance is better
            if (currentChance > currentBestChance || currentBestChance == 0) {
                currentBestChance = currentChance;
                result = c;
            }
        }
        System.out.println("-|- Classification complete");
        return result;
    }

    /**
     * Cleans the given words of too frequent and too less frequent words.
     * @param inWords The word list that needs to be cleaned.
     * @param minFrequency The min frequency a word has to occur to not get removed.
     * @param maxFrequency The max frequency a word has to occur to not get removed.
     * @return the cleaned list.
     */
    public List<String> cleanVocabulary(List<String> inWords, int minFrequency, int maxFrequency) {
        ArrayList<String> wordsToRemove = new ArrayList<>();

        Map<String, Integer> wordsWithCount = new HashMap<>();
        for (String word : inWords) {
            if (!wordsWithCount.containsKey(word)) {
                wordsWithCount.put(word, 1);
            } else {
                wordsWithCount.replace(word, wordsWithCount.get(word) + 1);
            }
        }

        for (String word : wordsWithCount.keySet()) {
            double frequency = wordsWithCount.get(word);
            if (frequency < minFrequency || frequency > maxFrequency) {
                wordsToRemove.add(word);
            }
        }

        List<String> result = new ArrayList<>();

        for (String word : inWords) {
            if (!wordsToRemove.contains(word)) {
                result.add(word);
            }
        }

        return result;
    }

    /**
     * Calculates the log2 of the given double.
     * @param in the input of the log2.
     * @return the log2 of the input.
     */
    private double log2(double in) {
        return Math.log(in)/Math.log(2);
    }


    public Map<String, Map<String, Double>> getChances() {
        return chances;
    }

    public void setChances(Map<String, Map<String, Double>> chances) {
        this.chances = chances;
    }
}