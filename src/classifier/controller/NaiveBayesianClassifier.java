package classifier.controller;

import classifier.model.Word;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class NaiveBayesianClassifier {

    private static Trainer trainer;
    private static Classifier classifier;
    private static Updater updater;

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

        trainer = new Trainer();
        classifier = new Classifier();
        //updater = new Updater("blogs");

        trainer.train();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            System.out.println(input + " to thee as well, melord.");
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

    public static int getSmoothingConstant() {
        return smoothingConstant;
    }

    public static void setSmoothingConstant(int smoothingConstant) {
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
