package classifier.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Wijtse on 21-1-2017.
 */
public class Updater {

    private String directory;
    private List<String> classes;

    public Updater(String directory) {
        //Determine classes
        File folder = new File(directory + File.separator + VocabularyBuilder.TRAIN_DIRECTORY_NAME);
        File[] classDirs = folder.listFiles();
        for (File file : classDirs) {
            if (file.isDirectory()) {
                classes.add(file.getName());
            }
        }
    }

    public void resetTrainingData() {
        for (String classDirectory : classes) {
            File classDirFile = new File(directory + File.separator + VocabularyBuilder.TRAIN_DIRECTORY_NAME + File.separator + classDirectory);
            File[] trainFiles = classDirFile.listFiles();
            for (File file : trainFiles) {
                if (file.isFile() && file.getName().contains("classified")) {
                    file.delete();
                }
            }
        }
    }

    public void copyToTrainingSet(File file, String className) {
        File destFile = new File(directory + File.separator + VocabularyBuilder.TRAIN_DIRECTORY_NAME + File.separator + className + File.separator + "classified" + file.getName());

    }
}
