package classifier.controller;

import classifier.model.Word;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Integer.min;

public class FeatureSelect {

    /**
     * Calculates the E table of the given word.
     * @param word word.
     * @return E table of the word.
     */
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

    /**
     * Calculates the Chi-square of the given word.
     * @param word word.
     * @return Chi-square of the word.
     */
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
                    if (Eij != 0) {
                        chisq = Math.pow(Mij - Eij, 2) / Eij;
                    } else {
                        chisq = 0;
                    }
                    result = result + chisq;
                }
            }
        }
        round(result, 2);
        return result;
    }

    /**
     * Returns a filtered feature list with only the best features.
     * @param in
     * @param n
     * @param c
     * @return
     */
    public static List<Word> getFeatures(List<Word> in, int n, String c) {
        List<Word> result = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            String maxC = "";
            double prevCVal = 0;
            for (int j = 0; j < in.get(i).getKeys().size() - 1; j++) {
                double curCVal = in.get(i).getM().get(in.get(i).getKeys().get(j)).get(0);
                if (curCVal > prevCVal) {
                    prevCVal = curCVal;
                    maxC = in.get(i).getKeys().get(j);
                }
            }
            if (maxC.equals(c)) {
                result.add(in.get(i));
            }
        }
        Collections.sort(result, new ChiSquaredComparator());
        Collections.reverse(result);
        if (result.size() > 0) {
            result = result.subList(0, min(result.size() - 1, n));
        }
        return result;
    }

    /**
     * Returns a filtered feature list with only the best features.
     * @param in
     * @param n
     * @return
     */
    public static List<Word> getFeaturesNaive(List<Word> in, int n) {
        List<Word> result = in;
        Collections.sort(result, new ChiSquaredComparator());
        Collections.reverse(result);
        if (result.size() > 0) {
            result = result.subList(0, min(result.size() - 1, n));
        }
        return result;
    }

    public static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
