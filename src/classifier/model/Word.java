package classifier.model;

import java.util.List;
import java.util.Map;

public class Word {

    private String word;
    private Map<String, List<Integer>> M;
    private Map<String, List<Integer>> E;

    public Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Map<String, List<Integer>> getM() {
        return M;
    }

    public void setM(Map<String, List<Integer>> m) {
        M = m;
    }

    public Map<String, List<Integer>> getE() {
        return E;
    }

    public void setE(Map<String, List<Integer>> e) {
        E = e;
    }
}
