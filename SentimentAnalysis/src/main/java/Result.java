import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code Result} class represents an analysis result of a single news article.
 * It encapsulates analysis details such as the news number, title, matched words, sentiment score, and polarity as attributes.
 */
public class Result {

    public int getNews_number() {
        return news_number;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getMatchedWords() {
        return matchedWords;
    }

    public int getScore() {
        return score;
    }

    public String getPolarity() {
        return polarity;
    }

    private int news_number;
    private String title;
    private List<String> matchedWords;
    private int score;
    private String polarity;

    /**
     * Constructs a {@code Result} instance with the passed details.
     *
     * @param news_number The index# of the news as in the MongoDb database.
     * @param title       The title of the news article.
     * @param matchedWords A {@code List<String>} of words that matched words in the sets present in {@link BagOfWords}.
     * @param score       The sentiment score calculated for the article.
     * @param polarity    The overall sentiment polarity for the article (Positive, Negative, or Neutral).
     */
    public Result(int news_number, String title, List<String> matchedWords, int score, String polarity) {
        this.news_number = news_number;
        this.title = title;
        this.matchedWords = new ArrayList<>(matchedWords); // Ensure it's a separate list instance
        this.score = score;
        this.polarity = polarity;
    }

}
