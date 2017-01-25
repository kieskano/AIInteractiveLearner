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

    private static Corpus corpus = Corpus.BLOGS;
    public static final int MAX_FEATURES = 20000;
    public static final int MAX_MIN_FREQ = 49;
    public static final int MAX_MAX_FREQ = 10000;

    private static int maxFeatures;
    private static int maxMinFreq;
    private static int maxMaxFreq;
    private static int incrFeat;
    private static int incrMaxFreq;
    private static int featStart;
    private static int minMaxFreq;

    public static void main(String[] args) {
        double currentBestResult = 0;
        List<Integer> bestVariables = new ArrayList<>();
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

        if (args.length > 0) {
            maxFeatures = Integer.parseInt(args[1]);
            maxMinFreq = Integer.parseInt(args[2]);
            maxMaxFreq = Integer.parseInt(args[3]);
            incrFeat = Integer.parseInt(args[4]);
            incrMaxFreq = Integer.parseInt(args[5]);
            featStart = Integer.parseInt(args[6]);
            minMaxFreq = Integer.parseInt(args[7]);

            for (int features = featStart; features < maxFeatures; features += incrFeat) {
                for (int minFreq = minMaxFreq; minFreq < maxMinFreq; minFreq++) {
                    for (int maxFreq = maxMinFreq + 1; maxFreq < maxMaxFreq; maxFreq += incrMaxFreq) {
                        System.out.println("Variables: [" + features + ", " + minFreq + ", " + maxFreq + "]");
                        double thisTestResult = megaTest(directory, features, 1, minFreq, maxFreq);
                        if (thisTestResult > currentBestResult) {
                            currentBestResult = thisTestResult;
                            bestVariables.clear();
                            bestVariables.add(features);
                            bestVariables.add(minFreq);
                            bestVariables.add(maxFreq);
                        }
                        System.out.println("CURRENT BEST RESULT: " + " " + bestVariables + " " + currentBestResult + "\n");
                    }
                }
            }
        } else {
            for (int features = 10; features < MAX_FEATURES; features += 10) {
                for (int minFreq = 1; minFreq < MAX_MIN_FREQ; minFreq++) {
                    for (int maxFreq = MAX_MIN_FREQ + 1; maxFreq < MAX_MAX_FREQ; maxFreq += 10) {
                        System.out.println("Variables: [" + features + ", " + minFreq + ", " + maxFreq + "]");
                        double thisTestResult = megaTest(directory, features, 1, minFreq, maxFreq);
                        if (thisTestResult > currentBestResult) {
                            currentBestResult = thisTestResult;
                            bestVariables.clear();
                            bestVariables.add(features);
                            bestVariables.add(minFreq);
                            bestVariables.add(maxFreq);
                        }
                        System.out.println("CURRENT BEST RESULT: " + " " + bestVariables + " " + currentBestResult + "\n");
                    }
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
            if (corpus.equals(Corpus.BLOGS)) {
                fileTrueClass = file.getName().substring(0, 1);
            } else if (corpus.equals(Corpus.MAIL)) {
                fileTrueClass = file.getName().substring(0,1).equals("s") ? "spam" : "ham";
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
            System.out.println(className + " " + (double)correctlyClassifiedFilesPerClass.get(className) /
                    (double)totalFilesPerClass.get(className) * 100);
            result = result + (double)correctlyClassifiedFilesPerClass.get(className) /
                    (double)totalFilesPerClass.get(className) * 100;
        }
        return result;
    }
}


//100% F 50% M
//2/3 F 1/3 M
//
