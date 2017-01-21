package classifier.controller;

import java.io.*;
import java.util.List;

/**
 * Created by Wijtse on 21-1-2017
 */
public class Updater {

    private String directory;
    private List<String> classes;

    public Updater() {
        directory = NaiveBayesianClassifier.getDirectory();
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
        try {
            copyFile(file, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFile(File src, File dest) throws  IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}