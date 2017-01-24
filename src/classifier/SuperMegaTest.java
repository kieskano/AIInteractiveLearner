package classifier;

import classifier.controller.Classifier;
import classifier.controller.NaiveBayesianClassifier;
import classifier.controller.Trainer;
import classifier.controller.Updater;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static classifier.controller.Classifier.TEST_DIRECTORY_NAME;

public class SuperMegaTest {

    public enum Corpus{MAIL, BLOGS};

    public static final Corpus CORPUS = Corpus.BLOGS;

    public static void main(String[] args) {
        double currentBestResult = 0;
        String directory = "";

        if (CORPUS.equals(Corpus.BLOGS)) {
            directory = "blogs";
        } else if (CORPUS.equals(Corpus.MAIL)) {
            directory = "mails";
        }




        System.out.println("RESULT IS: " + currentBestResult);
    }

    public double megaTest(String directory, int amountOfFeatures, double smoothingConstant, int min, int max) {
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
        for (String className : NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getClasses()) {
            System.out.println("Classified " + correctlyClassifiedFilesPerClass.get(className)
                    + " of " + totalFilesPerClass.get(className)
                    + " " + className + "-files correctly as " + className + "\t("
                    + (double)correctlyClassifiedFilesPerClass.get(className) /
                    (double)totalFilesPerClass.get(className) * 100 + "%)");
        }
        System.out.println((double)correctlyClassified / (double)testFiles.length * 100 + "% correctly classified");
        return (double)correctlyClassified / (double)testFiles.length * 100;
    }
}
