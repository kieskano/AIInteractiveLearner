package classifier.controller;

import classifier.model.Word;

import java.util.Comparator;

/**
 * Created by han on 14-1-17.
 */
public class ChiSquaredComparator implements Comparator<Word> {

    @Override
    public int compare(Word word1, Word word2) {
        return Double.compare(word1.getChisq(), word2.getChisq());
    }
}
