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

public class SuperMegaTest {

    public enum Corpus{MAIL, BLOGS};

    public static final Corpus CORPUS = Corpus.BLOGS;
    public static final int MAX_FEATURES = 100;
    public static final int MAX_MIN_FREQ = 49;
    public static final int MAX_MAX_FREQ = 100;

    public static void main(String[] args) {
        double currentBestResult = 0;
        List<Integer> bestVariables = new ArrayList<>();
        String directory = "";

        if (CORPUS.equals(Corpus.BLOGS)) {
            directory = "blogs";
        } else if (CORPUS.equals(Corpus.MAIL)) {
            directory = "mails";
        }

        for (int features = 10; features < MAX_FEATURES; features += 10) {
            for (int minFreq = 1; minFreq < MAX_MIN_FREQ; minFreq++) {
                for (int maxFreq = MAX_MIN_FREQ + 1; maxFreq < MAX_MAX_FREQ; maxFreq += 10) {
                    double thisTestResult = megaTest(directory, features, 1, minFreq, maxFreq);
                    if (thisTestResult > currentBestResult) {
                        currentBestResult = thisTestResult;
                        bestVariables.clear();
                        bestVariables.add(features);
                        bestVariables.add(minFreq);
                        bestVariables.add(maxFreq);
                    }
                    System.out.println("CURRENT BEST RESULT: " + " " + bestVariables + currentBestResult + "\n");
                }
            }
        }




        System.out.println("RESULT IS: " + currentBestResult + " " + bestVariables);
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
            if (CORPUS.equals(Corpus.BLOGS)) {
                fileTrueClass = file.getName().substring(0, 1);
            } else if (CORPUS.equals(Corpus.MAIL)) {
                fileTrueClass = "SP";
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
            result = result + (double)correctlyClassifiedFilesPerClass.get(className) /
                    (double)totalFilesPerClass.get(className) * 100;
        }
        return result;
    }
}


//100% F 50% M
//2/3 F 1/3 M
//
