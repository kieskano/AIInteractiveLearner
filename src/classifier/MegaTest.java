package classifier;

import classifier.controller.Classifier;
import classifier.controller.NaiveBayesianClassifier;
import classifier.controller.Trainer;
import classifier.controller.Updater;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static classifier.controller.Classifier.TEST_DIRECTORY_NAME;

/**
 * Created by Wijtse on 22-1-2017.
 */
public class MegaTest {

    public static void main(String[] args) {
        //Setup
        NaiveBayesianClassifier.setDirectory(args[0]);
        NaiveBayesianClassifier.setAmountOfFeatures(Integer.parseInt(args[1]));
        NaiveBayesianClassifier.setSmoothingConstant(Double.parseDouble(args[2]));
        NaiveBayesianClassifier.setMinFreq(Integer.parseInt(args[3]));
        NaiveBayesianClassifier.setMaxFreq(Integer.parseInt(args[4]));

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
            String fileTrueClass = file.getName().substring(0,1); // String fileTrueClass = file.getName().substring(0,1).equals("s") ? "spam" : "ham";
            String result = NaiveBayesianClassifier.getClassifier().classify(file.getName());
            if (result.equals(fileTrueClass)) {//(result.equals("spam") && fileTrueClass.equals("")) || (!result.equals("spam") && !fileTrueClass.equals(""))) {
                correctlyClassified++;
                correctlyClassifiedFilesPerClass.replace(fileTrueClass, correctlyClassifiedFilesPerClass.get(fileTrueClass) + 1);
            }
            totalFilesPerClass.replace(fileTrueClass, totalFilesPerClass.get(fileTrueClass) + 1);
        }

        //Print results
        System.out.println("");
        for (String className : NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getClasses()) {
            System.out.println("Classified " + correctlyClassifiedFilesPerClass.get(className) + " of " + totalFilesPerClass.get(className)
                    + " " + className + "-files correctly as " + className + "\t(" + (double)correctlyClassifiedFilesPerClass.get(className)/(double)totalFilesPerClass.get(className)*100 + "%)");
        }
        System.out.println((double)correctlyClassified/(double)testFiles.length*100 + "% correctly classified");
    }

//    ARGS:
//    blogs 800 1 5 6000
//    RESULT:
//    Classified 15 of 25 F-files correctly as F	(60.0%)
//    Classified 12 of 25 M-files correctly as M	(48.0%)
//    54.0% correctly classified

//    ARGS:
//    blogs 2000 1 5 6000
//    RESULT:
//    Classified 17 of 25 F-files correctly as F	(68.0%)
//    Classified 13 of 25 M-files correctly as M	(52.0%)
//    60.0% correctly classified

//    ARGS:
//    blogs 999999 1 0 999999999
//    RESULT:
//    Classified 16 of 25 F-files correctly as F	(64.0%)
//    Classified 10 of 25 M-files correctly as M	(40.0%)
//    52.0% correctly classified


//    [600, 6, 16051] 186%
//    [1100, 1, 18551] 190%

    //74% 2500 1 8 8000 blogs
    //80    2700 1 8 1550
}
