import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code Analyser} class is responsible for analysis of news titles,to determine the overall sentiment (positive, negative, or neutral)
 * based on predefined list of positive and negative words.
 */
public class Analyser {
    private DBManger db;
    private BagOfWords bagOfWords;
    private Writer writer;

    /**
     * Constructer to initialize an instance with specified database manager, bag of words,
     * and writer objects
     *
     * @param db The database manager object to retrieve the news titles from.
     * @param bagOfWords The BagOfWords object containing the sets of positive and negative words.
     * @param writer The writer object to output the analysis results into csv.
     */
    public Analyser(DBManger db, BagOfWords bagOfWords, Writer writer) {
        this.db = db;
        this.bagOfWords = bagOfWords;
        this.writer = writer;
    }

    /**
     * Analyses the sentiment of titles retrieved from MongoDb database. It calculates a score based
     * on the occurrence of the words in the title, in the positive and negative sets and assigns a polarity (positive,
     * negative, or neutral) based on the score of the title.
     */
    public void analyseTitles() {
        // Form the database Connection
        db.Connect();

        List<String> titles = db.getTitles();
        List<Result> results = new ArrayList<>();

        // Default News number
        int news_number = 1;
        for (String title : titles) {
            List<String> matchedWords = new ArrayList<>();
            int score = 0;
            String[] words = title.split("\\W+");

            for (String word : words) {
                if (bagOfWords.getNegatives().contains(word.trim().toLowerCase())) {
                    score--;
                    matchedWords.add(word);
                }
                if (bagOfWords.getPositives().contains(word.trim().toLowerCase())) {
                    score++;
                    matchedWords.add(word);
                }
            }

            String polarity = determinePolarity(score);
            results.add(new Result(news_number++, title.toUpperCase(), matchedWords, score, polarity));

        }

        // Write results to file
        writer.writeResults(results);
    }

    /**
     * Determines the polarity of a title based on its sentiment score.
     *
     * @param score The sentiment score of a title.
     * @return A string representing the polarity of the passed score: "Positive", "Negative", or "Neutral".
     */
    public String determinePolarity(int score){
        if(score>0)
            return "Positive";
        else if(score<0){
            return "Negative";
        }
        else{
            return "Neutral";
        }
    }


}
