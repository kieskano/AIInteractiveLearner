package classifier.test;

import classifier.controller.FeatureSelect;
import classifier.controller.VocabularyBuilder;
import classifier.model.Word;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by han on 25-1-17.
 */
public class ChiTest {

    private static BufferedWriter bw = null;
    private static FileWriter fw = null;

    public static void main(String[] args) {
        try {
            VocabularyBuilder vocabularyBuilder = new VocabularyBuilder("blogs");
            vocabularyBuilder.loadWords();
            vocabularyBuilder.cleanVocabulary(1, 99999999);

            for (Word word : vocabularyBuilder.getWordList()) {
                word.setE(FeatureSelect.getE(word));
                word.setChisq(FeatureSelect.getChisq(word));
            }

            List<Word> content = FeatureSelect.getFeaturesNaive(vocabularyBuilder.getWordList(), 1000);

            fw = new FileWriter("chisqout" + File.separator + "chi-words-blogs.txt");
            bw = new BufferedWriter(fw);
            bw.write(content.toString());

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }
}
