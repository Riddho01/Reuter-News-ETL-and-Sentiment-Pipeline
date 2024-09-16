
/**
 * The {@code Main} class serves as the entry point to the application.
 * It initializes all the required components and triggers the analysis process.
 */
public class Main {
    public static void main(String[] args) {
        DBManger dbManager = new DBManger();
        BagOfWords bagOfWords = new BagOfWords();
        Writer writer = new Writer();

        Analyser analyser = new Analyser(dbManager, bagOfWords, writer);
        analyser.analyseTitles();
    }
}