package classifier.controller;

import classifier.model.Word;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wijtse on 14-1-2017.
 */
public class VocabularyBuilder {

    public static final String TRAIN_DIRECTORY_NAME = "train";

    private HashMap<String, Word> words = new HashMap<>();
    private String directory;
    private HashMap<String, ArrayList<String>> files = new HashMap<>();
    private HashMap<String, Double> numberOfFiles = new HashMap<>();
    private ArrayList<String> classes = new ArrayList<>();


    public VocabularyBuilder(String derectory) {
        this.directory = derectory;

        //Determine classes
        File folder = new File(derectory);
        File[] classDirs = folder.listFiles();
        for (File file : classDirs) {
            if (file.isDirectory()) {
                files.put(file.getName(), new ArrayList<>());
                numberOfFiles.put(file.getName(), 0.0);
                classes.add(file.getName());
            }
        }

        //Determine filenames
        for (String classDirectory : classes) {
            File classDirFile = new File(derectory);
            File[] trainFiles = classDirFile.listFiles();
            for (File file : trainFiles) {
                if (file.isFile()) {
                    files.get(classDirectory).add(file.getName());
                    numberOfFiles.replace(classDirectory, numberOfFiles.get(classDirectory) + 1);
                }
            }
        }

        System.out.println("Located classes:");
        for (String className : classes) {
            System.out.println("'" + className + "' with " + numberOfFiles.get(className) +  " training files");
        }
    }

    public void loadWords() {
        for (String classDirectory : classes) {
            for (String fileName : files.get(classDirectory)) {
                List<String> lines = null;
                try {
                    lines = Files.readAllLines(Paths.get(directory + File.pathSeparator + File.pathSeparator + classDirectory + fileName), Charset.defaultCharset());
                } catch (IOException e) {
                    System.out.println("Could not read lines from file: " + fileName + " Because: " + e);
                    System.exit(1);
                }

                for (String line : lines) {
                    String[] fileWords = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
                    for (String word : fileWords) {
                        //If the word does not exist
                        if (words.containsKey(word)) {
                            Word newWord = new Word(word);
                            Map<String, List<Double>> m = new HashMap<>();
                            for (String className : classes) {
                                List<Double> ints = new ArrayList<>();
                                if (className.equals(classDirectory)) {
                                    ints.add(1.0);
                                } else {
                                    ints.add(0.0);
                                }
                                ints.add(0.0);
                                ints.add(numberOfFiles.get(className));
                                m.put(className, ints);
                            }
                            newWord.setM(m);
                            newWord.setKeys(classes);
                        } else { //If the word does exist
                            Word wordObj = words.get(word);
                            wordObj.getM().get(classDirectory).set(0, wordObj.getM().get(classDirectory).get(0) + 1);
                        }
                    }
                }
            }
        }
    }
}
