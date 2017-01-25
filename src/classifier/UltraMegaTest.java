package classifier;
import classifier.controller.Classifier;
import classifier.controller.NaiveBayesianClassifier;
import classifier.controller.Trainer;
import classifier.controller.Updater;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static classifier.controller.Classifier.TEST_DIRECTORY_NAME;

public class UltraMegaTest {

    public enum Corpus {MAIL, BLOGS};

    private static Corpus corpus = Corpus.BLOGS;
    public static void main(String[] args) {
        Map<Integer, Double> results = new HashMap<>();
        List<Integer> keys = new ArrayList<>();
        String directory = "";

        if (args[0].equals("blogs")) {
            corpus = Corpus.BLOGS;
        } else if (args[0].equals("mail")) {
            corpus = Corpus.MAIL;
        }

        if (corpus.equals(Corpus.BLOGS)) {
            directory = "blogs";
        } else if (corpus.equals(Corpus.MAIL)) {
            directory = "mails";
        }

        for (int features = 100; features <= 3500; features += 100) {
            System.out.println("Vocab size: [" + features + "]");
            double thisTestResult = megaTest(directory, features, 1, 1, 13050);
            keys.add(features);
            results.put(features, thisTestResult);
        }

        System.out.println("RESULTS:");
        for (int key : keys) {
            System.out.println(key + "\t\t||\t\t" + results.get(key));
        }
    }

    public static double megaTest(String directory, int amountOfFeatures, double smoothingConstant, int min, int max) {
        NaiveBayesianClassifier.setDirectory(directory);
        NaiveBayesianClassifier.setAmountOfFeatures((amountOfFeatures));
        NaiveBayesianClassifier.setSmoothingConstant((smoothingConstant));
        NaiveBayesianClassifier.setMinFreq((min));
        NaiveBayesianClassifier.setMaxFreq((max));

        NaiveBayesianClassifier.setTrainer(new Trainer());
        NaiveBayesianClassifier.setClassifier(new Classifier());
        NaiveBayesianClassifier.setUpdater(new Updater());

        NaiveBayesianClassifier.getUpdater().resetTrainingData();

        NaiveBayesianClassifier.getTrainer().train();

        Map<String, Integer> totalFilesPerClass = new HashMap<>();
        Map<String, Integer> correctlyClassifiedFilesPerClass = new HashMap<>();
        for (String className : NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getClasses()) {
            totalFilesPerClass.put(className, 0);
            correctlyClassifiedFilesPerClass.put(className, 0);
        }

        //Do testing
        File folder = new File(NaiveBayesianClassifier.getDirectory() + File.separator + TEST_DIRECTORY_NAME);
        File[] testFiles = folder.listFiles();
        int correctlyClassified = 0;
        for (File file : testFiles) {
            String fileTrueClass = "";
            if (corpus.equals(UltraMegaTest.Corpus.BLOGS)) {
                fileTrueClass = file.getName().substring(0, 1);
            } else if (corpus.equals(UltraMegaTest.Corpus.MAIL)) {
                fileTrueClass = file.getName().substring(0, 1).equals("s") ? "spam" : "ham";
            }
            String result = NaiveBayesianClassifier.getClassifier().classify(file.getName());
            if (result.equals(fileTrueClass)) {
                correctlyClassified++;
                correctlyClassifiedFilesPerClass.replace(fileTrueClass,
                        correctlyClassifiedFilesPerClass.get(fileTrueClass) + 1);
            }
            totalFilesPerClass.replace(fileTrueClass, totalFilesPerClass.get(fileTrueClass) + 1);
        }

        //Print results
        System.out.println("");
        double result = 0.0;
        for (String className : NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getClasses()) {
            System.out.println(className + " " + (double) correctlyClassifiedFilesPerClass.get(className) /
                    (double) totalFilesPerClass.get(className) * 100);
        }
        return (double)correctlyClassified/(double)testFiles.length*100;
    }
}


//100% F 50% M
//2/3 F 1/3 M
//
