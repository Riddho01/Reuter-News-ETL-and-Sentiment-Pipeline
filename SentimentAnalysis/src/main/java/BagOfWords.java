import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * This class manages two sets: "positives" and "negatives". The two sets are populated by reading
 * negative and positive words from text files.
 */
public class BagOfWords {

    // Sets for negative and positive words
    private static Set<String> negatives = new HashSet<>();
    private static Set<String> positives = new HashSet<>();

    /**
     * Constructor to initialize the sets of negative and
     * positive words from their respective text files.
     */
    public BagOfWords() {
        negatives = readWordsFromFile("negative-words.txt");
        positives = readWordsFromFile("positive-words.txt");
    }

    /**
     * Provides access to the set of negative words.
     *
     * @return A set containing negative words.
     */
    public static Set<String> getNegatives() {
        return negatives;
    }
    /**
     * Provides access to the set of positive words.
     *
     * @return A set containing positive words.
     */
    public static Set<String> getPositives() {
        return positives;
    }

    /**
     * Method to read positive/negative words from a specified file and add them to a set.
     * Each word is processed through trimming and converting to lowercase before being added to ensure consistency.
     *
     * @param fileName The name of the file containing the words to be loaded.
     * @return A {@code Set<String>} containing the words read from the file.
     * @throws RuntimeException in case, the file does not exist in the path or cannot be read
     */
    private Set<String> readWordsFromFile(String fileName) {
        Set<String> words = new HashSet<>();

        InputStream inputStream = getClass().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new RuntimeException("Could not locate " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (!line.trim().isEmpty()) {
                    words.add(line.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }

        return words;
    }
}
