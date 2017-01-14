package classifier.model;

import classifier.controller.FeatureSelect;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Word {

    private String word;
    private Map<String, List<Double>> M = new HashMap<>();
    private Map<String, List<Double>> E = new HashMap<>();
    private double chisq = 0;
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

    public Map<String, List<Double>> getM() {
        return M;
    }

    public void setM(Map<String, List<Double>> m) {
        M = m;
    }

    public Map<String, List<Double>> getE() {
        return E;
    }

    public void setE(Map<String, List<Double>> e) {
        E = e;
    }

    public double getChisq() {
        return chisq;
    }

    public void setChisq(double chisq) {
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
                result = result + "\n";
                for (int i = 0; i <= keys.size(); i++) {
                    result = result + "-----------";
                }
                result = result + "\n W\t\t";
                for (String key : keys) {
                    if (M.keySet().contains(key)) {
                        result = result + "|  " + FeatureSelect.round(M.get(key).get(0), 1) + "\t\t";
                    }
                }
                result = result + "\n nW\t\t";
                for (String key : keys) {
                    if (M.keySet().contains(key)) {
                        result = result + "|  " + FeatureSelect.round(M.get(key).get(1), 1) + "\t\t";
                    }
                }
                result = result + "\n T\t\t";
                for (String key : keys) {
                    if (M.keySet().contains(key)) {
                        result = result + "|  " + FeatureSelect.round(M.get(key).get(2), 1) + "\t\t";
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
                result = result + "\n";
                for (int i = 0; i <= keys.size(); i++) {
                    result = result + "-----------";
                }
                result = result + "\n W\t\t";
                for (String key : keys) {
                    if (E.keySet().contains(key)) {
                        result = result + "|  " + FeatureSelect.round(E.get(key).get(0), 1) + "\t\t";
                    }
                }
                result = result + "\n nW\t\t";
                for (String key : keys) {
                    if (E.keySet().contains(key)) {
                        result = result + "|  " + FeatureSelect.round(E.get(key).get(1), 1) + "\t\t";
                    }
                }
                result = result + "\n T\t\t";
                for (String key : keys) {
                    if (E.keySet().contains(key)) {
                        result = result + "|  " + FeatureSelect.round(E.get(key).get(2), 1) + "\t\t";
                    }
                }
                result = result + "\n";
            } else {
                result = result + "=> E: No list E.\n";
            }
        }
        result = result + "=> Chi-squared: " + chisq + "\n";
        result = result + "----------------------END WORD------------------------";
        return result;
    }
}
