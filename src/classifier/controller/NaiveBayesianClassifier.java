package classifier.controller;

import classifier.model.Word;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by han on 21-1-17.
 */
public class NaiveBayesianClassifier {

    private static Trainer trainer;
    private static Classifier classifier;
    //private static Updater updater;



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
        //updater = new Updater();


    }
}
