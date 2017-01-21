package classifier.controller;

import classifier.model.Document;
import classifier.model.Word;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Wijtse on 17-1-2017.
 */
public class Classifier {

    private HashMap<String, Word> vocabulair;

    public Classifier(List<Word> vocabulair) {
        this.vocabulair = new HashMap<>();
        for (Word word : vocabulair) {
            this.vocabulair.put(word.getWord(), word);
        }
    }

    public String classify(Document document) {
        String result = "";


        return result;
    }

}
