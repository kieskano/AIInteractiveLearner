package classifier.controller;


import classifier.model.Word;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Classifier {

    public static final String TEST_DIRECTORY_NAME = "test";

    private List<String> words;
    private List<String> documentFeatures;
    private Map<String, Integer> totalWordCount;
    private Map<String, Map<String, Double>> chances;
    private Map<String, Integer> wordCountPerDocument;

    public String classify(String filename) {
        words = new ArrayList<>();
        documentFeatures = new ArrayList<>();
        chances = new HashMap<>();
        totalWordCount = new HashMap<>();
        wordCountPerDocument = new HashMap<>();

        System.out.println("-|- Started classification");
        String result = "";
        String fileLocation = NaiveBayesianClassifier.getDirectory() + File.separator + TEST_DIRECTORY_NAME
                + File.separator + filename;
        File unclassifiedFile = new File(fileLocation);
        System.out.println("-|- Reading file...");
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(unclassifiedFile.toPath());
        } catch (IOException e) {
            System.out.println("-|- Error reading file, will now exit");
            System.exit(1);
        }
        System.out.println("-|- File read");
        System.out.println("-|- Normalizing words...");
        for (String line : lines) {
            String[] wordArray = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
            words = Arrays.asList(wordArray);
        }
        System.out.println("-|- Words normalized");

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
            Map<String, Double> chancesPerWord = new HashMap<>();
            if (!documentFeatures.contains(word) && features.containsKey(word)) {
                documentFeatures.add(word);
                wordCountPerDocument.put(word, 1);
                for (String c : NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getClasses()) {
                    Word currentFeature = features.get(word);
                    int occurrencesPerWord= currentFeature.getOccurrencesPerClass().get(c);
                    double smoothing = NaiveBayesianClassifier.getSmoothingConstant();
                    double chance = (occurrencesPerWord + smoothing) /
                            (totalWordCount.get(c) + (smoothing * features.size()));
                    chancesPerWord.put(c, chance);
                }
                chances.put(word, chancesPerWord);
            } else if (features.containsKey(word)) {
                wordCountPerDocument.replace(word, wordCountPerDocument.get(word) + 1);
            }
        }
        System.out.println("-|- Calculating chances for class given the new document...");
        double currentBestChance = 0;
        Map<String, Double> numberOfFiles =
                NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getNumberOfFiles();
        double totalNrOfFiles = NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getTotalNrOfFiles();
        for (String c : NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getClasses()) {
            double numberOfFilesPerClass = numberOfFiles.get(c);
            double chanceOfClass = numberOfFilesPerClass / totalNrOfFiles;
            double currentChance = log2(chanceOfClass);
            for (String word : documentFeatures) {
                for (int i = 0; i < wordCountPerDocument.get(word); i++) {
                    currentChance += log2(chances.get(word).get(c));
                }
            }
            System.out.println(c + " " + currentChance);
            //log2(0.001) = -9 log2(0.0001) = -13 so the higher chance is better
            if (currentChance > currentBestChance || currentBestChance == 0) {
                currentBestChance = currentChance;
                result = c;
            }
        }
        System.out.println("RESULT CLASSIFIER = " + result);

        return result;
    }

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
