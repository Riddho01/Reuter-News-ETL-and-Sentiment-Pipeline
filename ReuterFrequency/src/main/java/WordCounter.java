import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.SparkSession;

import java.util.*;
import java.util.regex.Pattern;

public class WordCounter {

    /**
     * This class uses Apache Spark to process a file containing
     * textual data about Reuters news, cleans the data removing unwanted tags, expressions and
     * words, filters out stop words. Then ,it computes the frequency of each word.
     */

    //Tags and HTML entities that need to be removed
    private static final Pattern TAGS = Pattern.compile("<.*?>|&lt;|>|&#[0-9]+;");

    //Characters not in the English alphabet
    private static final Pattern NON_LETTERS = Pattern.compile("[^a-zA-Z ]");

    //Word to remove: reuter occurring at the end of each reuter element
    private static final String WORD_TO_REMOVE = "reuter";

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("Word Count")
                .config("spark.master", "local")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        // get the stop Words
        JavaRDD<String> stopWords = sc.textFile("/user/riddhoriddho/Stop_Words.txt");

        // Broadcast stop words to each node
        final Set<String> stopWordsSet = new HashSet<>(stopWords.collect());

        // Load the dataset
        JavaRDD<String> data = sc.textFile("/user/riddhoriddho/reut2-009.sgm");

        // Clean data by removing unwanted expressions and tags
        JavaRDD<String> words = data.flatMap(
                        (FlatMapFunction<String, String>) s -> {
                            s = TAGS.matcher(s).replaceAll("");
                            s = NON_LETTERS.matcher(s).replaceAll(" ");
                            return Arrays.asList(s.toLowerCase().split("\\s+")).iterator();
                        })
                .filter(s -> !stopWordsSet.contains(s) && s.length() > 0 && !s.equals(WORD_TO_REMOVE));

        // Count the frequency of each word
        Map<String, Long> wordCounts = words.countByValue();

        // Find and display the most and least frequent words in the reuters file
        displayWords(wordCounts);

        //Terminate the Spark job
        spark.stop();
    }

    /**
     * Display the most and least frequently occurring words found in the file.
     *
     * @param wordCounts Map of words and their corresponding frequencies in the file.
     */
    private static void displayWords(Map<String, Long> wordCounts) {

        List<Map.Entry<String, Long>> sorted = new ArrayList<>(wordCounts.entrySet());
        sorted.sort(Map.Entry.comparingByValue());

        Map.Entry<String, Long> mostFrequent = sorted.get(sorted.size() - 1);
        Map.Entry<String, Long> leastFrequent = sorted.get(0);

        System.out.println("Most Frequent Word: " + mostFrequent.getKey() + " - " + mostFrequent.getValue());
        System.out.println("Least Frequent Word: " + leastFrequent.getKey() + " - " + leastFrequent.getValue());
    }
}
