package classifier.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wijtse on 17-1-2017.
 */
public class Document {

    private Map<String, Integer> words = new HashMap<>();

    public Document() {

    }

    public Map<String, Integer> getWords() {
        return words;
    }
}
