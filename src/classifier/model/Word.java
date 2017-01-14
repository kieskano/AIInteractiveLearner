package classifier.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Word {

    private String word;
    private Map<String, List<Integer>> M = new HashMap<>();
    private Map<String, List<Integer>> E = new HashMap<>();
    private int chisq = 0;
    private List<String> keys = new ArrayList<>();

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

    public int getChisq() {
        return chisq;
    }

    public void setChisq(int chisq) {
        this.chisq = chisq;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public String toString() {
        String result = "";
        result = result + "----------------------BEGIN WORD----------------------";
        result = result + "\n=> Word: " + word + "\n";
        if (keys.size() > 0) {
            if (M.size() > 0) {
                result = result + "=> M:\n";
                result = result + "\t\t";
                for (String key : keys) {
                    if (M.keySet().contains(key)) {
                        result = result + "|  " + key + "\t\t";
                    }
                }
                result = result + "\n-------------------------------------------";
                result = result + "\n W\t\t";
                for (String key : keys) {
                    if (M.keySet().contains(key)) {
                        result = result + "|  " + M.get(key).get(0) + "\t\t";
                    }
                }
                result = result + "\n nW\t\t";
                for (String key : keys) {
                    if (M.keySet().contains(key)) {
                        result = result + "|  " + M.get(key).get(1) + "\t\t";
                    }
                }
                result = result + "\n T\t\t";
                for (String key : keys) {
                    if (M.keySet().contains(key)) {
                        result = result + "|  " + M.get(key).get(2) + "\t\t";
                    }
                }
                result = result + "\n";
            } else {
                result = result + "=> M: No list M.\n";
            }
            if (E.size() > 0) {
                result = result + "=> E:\n";
                result = result + "\t\t";
                for (String key : keys) {
                    if (E.keySet().contains(key)) {
                        result = result + "|  " + key + "\t\t";
                    }
                }
                result = result + "\n-------------------------------------------";
                result = result + "\n W\t\t";
                for (String key : keys) {
                    if (E.keySet().contains(key)) {
                        result = result + "|  " + E.get(key).get(0) + "\t\t";
                    }
                }
                result = result + "\n nW\t\t";
                for (String key : keys) {
                    if (E.keySet().contains(key)) {
                        result = result + "|  " + E.get(key).get(1) + "\t\t";
                    }
                }
                result = result + "\n T\t\t";
                for (String key : keys) {
                    if (E.keySet().contains(key)) {
                        result = result + "|  " + E.get(key).get(2) + "\t\t";
                    }
                }
                result = result + "\n";
            } else {
                result = result + "=> E: No list E.\n";
            }
        }
        result = result + "----------------------END WORD------------------------";
        return result;
    }
}
