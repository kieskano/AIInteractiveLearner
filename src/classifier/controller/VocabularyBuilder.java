package classifier.controller;

import classifier.model.Word;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Wijtse on 14-1-2017
 */
public class VocabularyBuilder {

    public static final String TRAIN_DIRECTORY_NAME = "train";
    public static final String M_ELEMENT_TOTAL_NAME = "total";

    private Map<String, Word> words = new HashMap<>();
    private Map<String, Integer> totalWordCounts = new HashMap<>();
    private Map<String, Map<String, Integer>> totalWordCountsPerClass = new HashMap<>();


    private String directory;
    private Map<String, ArrayList<String>> files = new HashMap<>();
    private Map<String, Double> numberOfFiles = new HashMap<>();
    private List<String> classes = new ArrayList<>();
    private double totalNrOfFiles = 0;

    public VocabularyBuilder(String derectory) {
        this.directory = derectory;

        //Determine classes
        File folder = new File(derectory + File.separator + TRAIN_DIRECTORY_NAME);
        File[] classDirs = folder.listFiles();
        for (File file : classDirs) {
            if (file.isDirectory()) {
                files.put(file.getName(), new ArrayList<>());
                numberOfFiles.put(file.getName(), 0.0);
                classes.add(file.getName());
                totalWordCountsPerClass.put(file.getName(), new HashMap<>());
            }
        }

        //Determine filenames
        for (String classDirectory : classes) {
            File classDirFile = new File(derectory + File.separator + TRAIN_DIRECTORY_NAME + File.separator + classDirectory);
            File[] trainFiles = classDirFile.listFiles();
            for (File file : trainFiles) {
                if (file.isFile()) {
                    files.get(classDirectory).add(file.getName());
                    numberOfFiles.replace(classDirectory, numberOfFiles.get(classDirectory) + 1);
                    totalNrOfFiles++;
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
                    lines = Files.readAllLines(Paths.get(directory + File.separator + TRAIN_DIRECTORY_NAME + File.separator + classDirectory + File.separator + fileName), Charset.defaultCharset());
                } catch (IOException e) {
                    System.out.println("Could not read lines from file: " + fileName + " Because: " + e);
                    System.exit(1);
                }
                Set<String> pastWords = new HashSet<>();
                for (String line : lines) {
                    String[] fileWords = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
                    for (String word : fileWords) {
                        if (totalWordCounts.containsKey(word)) {
                            totalWordCounts.replace(word, totalWordCounts.get(word) + 1);
                            totalWordCountsPerClass.get(classDirectory).replace(word, totalWordCountsPerClass.get(classDirectory).get(word) + 1);
                        } else {
                            totalWordCounts.put(word, 1);
                            totalWordCountsPerClass.get(classDirectory).put(word, 1);
                        }
                        if (!pastWords.contains(word)) { //Checks for double words in the same file
                            pastWords.add(word);
                            //If the word does not exist
                            if (!words.containsKey(word)) {
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
                                List<String> keys = new ArrayList<>();
                                keys.addAll(classes);
                                newWord.setKeys(keys);
                                words.put(word, newWord);
                            } else { //If the word does exist
                                Word wordObj = words.get(word);
                                wordObj.getM().get(classDirectory).set(0, wordObj.getM().get(classDirectory).get(0) + 1);
                            }
                        }
                    }
                }
            }
        }

        for (Word word : words.values()) {
            double totalIn = 0;
            double totalNotIn = 0;
            for (String className : classes) {
                List<Double> mCol =  word.getM().get(className);
                mCol.set(1, mCol.get(2) - mCol.get(0));
                totalIn += mCol.get(0);
                totalNotIn += mCol.get(1);
            }
            List<Double> lastCol = new ArrayList<>();
            lastCol.add(totalIn);
            lastCol.add(totalNotIn);
            lastCol.add(totalNrOfFiles);
            word.getM().put(M_ELEMENT_TOTAL_NAME, lastCol);
            word.getKeys().add(M_ELEMENT_TOTAL_NAME);

            Map<String, Integer> occurencesPerClass = new HashMap<>();
            for (String className : classes) {
                occurencesPerClass.put(className, 0);
                if (totalWordCountsPerClass.get(className).get(word.getWord()) != null) {
                    occurencesPerClass.replace(className, totalWordCountsPerClass.get(className).get(word.getWord()));
                }
            }
            word.setOccurrencesPerClass(occurencesPerClass);
        }
        //System.out.println(words.get("people").toString());
    }

    public void cleanVocabulary(int minFrequency, int maxFrequency) {
        ArrayList<String> wordsToRemove = new ArrayList<>();
        for (String word : words.keySet()) {
            double frequency = totalWordCounts.get(word);
            if (frequency < minFrequency || frequency > maxFrequency) {
                wordsToRemove.add(word);
            }
        }

        for (String word : wordsToRemove) {
            words.remove(word);
        }

        //Hardcoded blacklist
        words.remove("");
    }

    public List<Word> getWordList() {
        List<Word> result = new ArrayList<>();

        result.addAll(words.values());

        return result;
    }

    public Map<String, Word> getWordMap() {
        return words;
    }

    public int getNumberOfTrainingFiles() {
        return (int) totalNrOfFiles;
    }

    public int getTotalNumberOfUniqueWords() {
        return words.size();
    }

    public int getTotalNumberOfWords() {
        int result = 0;

        //TODO do dis

        return result;
    }

    public static void main(String[] args) {
//        VocabularyBuilder vb = new VocabularyBuilder("blogs");
//        vb.loadWords();
//        System.out.println(vb.getWordMap().get("hello").toString());

    }
}
