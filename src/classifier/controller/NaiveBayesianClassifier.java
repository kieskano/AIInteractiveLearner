package classifier.controller;

import classifier.model.Word;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static classifier.controller.Classifier.TEST_DIRECTORY_NAME;


public class NaiveBayesianClassifier {

    private static Trainer trainer;
    private static Classifier classifier;
    private static Updater updater;

    private static String directory;
    private static int amountOfFeatures;
    private static double smoothingConstant;
    private static int minFreq;
    private static int maxFreq;

    public static void main(String[] args) {
        //Parse args
        directory = args[0];
        amountOfFeatures = Integer.parseInt(args[1]);
        smoothingConstant = Double.parseDouble(args[2]);
        minFreq = Integer.parseInt(args[3]);
        maxFreq = Integer.parseInt(args[4]);

        trainer = new Trainer();
        classifier = new Classifier();
        updater = new Updater();

        updater.resetTrainingData();

        trainer.train();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String filename = sc.nextLine();
            String result = classifier.classify(filename);
            System.out.println("--| Started updating");
            System.out.println("--| The file is classified as: " + result);
            boolean inputValid = false;
            String correct = "";
            String actualClass = "";
            while (!inputValid) {
                System.out.println("--| Is this correct? (y/n)");
                correct = sc.nextLine();
                if (correct.equals("y")) {
                    inputValid = true;
                    actualClass = result;
                } else if (correct.equals("n")) {
                    inputValid = true;
                    boolean isActualClass = false;
                    while (!isActualClass) {
                        System.out.println("--| Could you please tell me what the right class would have been?");
                        actualClass = sc.nextLine();
                        if (trainer.getVocabularyBuilder().getClasses().contains(actualClass)) {
                            isActualClass = true;
                        } else {
                            System.out.println("--| Unfortunately, that class is not known. Please try again");
                        }
                    }
                } else {
                    System.out.println("--| Please answer with \"y\" or \"n\"");
                }
            }
            String fileLocation = NaiveBayesianClassifier.getDirectory() + File.separator + TEST_DIRECTORY_NAME
                    + File.separator + filename;
            File classifiedFile = new File(fileLocation);
            updater.copyToTrainingSet(classifiedFile, actualClass);
            System.out.println("--| Retraining...");
            trainer.train();
            System.out.println("--| Updating complete");
        }


    }

    public static Trainer getTrainer() {
        return trainer;
    }

    public static void setTrainer(Trainer trainer) {
        NaiveBayesianClassifier.trainer = trainer;
    }

    public static Classifier getClassifier() {
        return classifier;
    }

    public static void setClassifier(Classifier classifier) {
        NaiveBayesianClassifier.classifier = classifier;
    }

    public static Updater getUpdater() {
        return updater;
    }

    public static void setUpdater(Updater updater) {
        NaiveBayesianClassifier.updater = updater;
    }

    public static String getDirectory() {
        return directory;
    }

    public static void setDirectory(String directory) {
        NaiveBayesianClassifier.directory = directory;
    }

    public static int getAmountOfFeatures() {
        return amountOfFeatures;
    }

    public static void setAmountOfFeatures(int amountOfFeatures) {
        NaiveBayesianClassifier.amountOfFeatures = amountOfFeatures;
    }

    public static double getSmoothingConstant() {
        return smoothingConstant;
    }

    public static void setSmoothingConstant(double smoothingConstant) {
        NaiveBayesianClassifier.smoothingConstant = smoothingConstant;
    }

    public static int getMinFreq() {
        return minFreq;
    }

    public static void setMinFreq(int minFreq) {
        NaiveBayesianClassifier.minFreq = minFreq;
    }

    public static int getMaxFreq() {
        return maxFreq;
    }

    public static void setMaxFreq(int maxFreq) {
        NaiveBayesianClassifier.maxFreq = maxFreq;
    }
}
