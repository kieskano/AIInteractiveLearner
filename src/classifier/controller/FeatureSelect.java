package classifier.controller;

import classifier.model.Word;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureSelect {

    public static Map<String, List<Double>> getE(Word word) {
        Map<String, List<Double>> result = new HashMap<>();
        Map<String, List<Double>> M = word.getM();
        List<String> keys = word.getKeys();
        double TW;
        double TnW;
        double N;
        double TC;
        double EW;
        double EnW;

        List<Double> T = M.get(keys.get(keys.size() - 1));
        TW = T.get(0);
        TnW = T.get(1);
        N = T.get(2);

        for (String key : keys) {
            if (!key.equals(keys.get(keys.size() - 1))) {
                TC = M.get(key).get(2);
                EW = TC * TW / N;
                EnW = TC * TnW / N;
                List<Double> newE = new ArrayList<>();
                newE.add(EW);
                newE.add(EnW);
                newE.add(TC);

                result.put(key, newE);
            }
        }
        result.put(keys.get(keys.size() - 1), M.get(keys.get(keys.size() - 1)));
        return result;
    }

    public static double getChisq(Word word) {
        double result = 0;
        Map<String, List<Double>> M = word.getM();
        Map<String, List<Double>> E = word.getE();
        List<String> keys = word.getKeys();
        double Mij;
        double Eij;
        double chisq;

        for (String key : keys) {
            if (!key.equals(keys.get(keys.size() - 1))) {
                for (int i = 0; i < 2; i++) {
                    Mij = M.get(key).get(i);
                    Eij = E.get(key).get(i);
                    chisq = Math.pow(Mij - Eij, 2) / Eij;
                    result = result + chisq;
                }
            }
        }

        round(result, 2);
        return result;
    }

    public static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
