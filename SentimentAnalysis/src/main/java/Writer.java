import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * The {@code Writer} class handles the writing the analysis results to a CSV file.
 */
public class Writer {

    /**
     * Writes the given list of {@link Result} objects to a CSV file.
     *
     * @param results The list of {@code Result} objects to be written to the file.
     */
    public void writeResults(List<Result> results) {

        String resultFilePath = "Analysis_Results.csv";

        try (FileWriter fw = new FileWriter(resultFilePath)) {

            // Write the column headers and add a newline character at the end
            fw.append("News#,Title Content,match,score,Polarity\n");

            for (Result result : results) {
                // Escape potential double quotes in the content
                String escapedTitle = result.getTitle().replace("\"", "\"\"");
                String escapedMatchedWords = String.join(";", result.getMatchedWords()).replace("\"", "\"\"");

                // Write the data, ensuring that any field with commas is quoted
                fw.append(String.join(",",
                        "\"" + result.getNews_number() + "\"",
                        "\"" + escapedTitle + "\"",
                        "\"" + escapedMatchedWords + "\"",
                        "\"" + result.getScore() + "\"",
                        "\"" + result.getPolarity() + "\""
                )).append("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not write results to file: " + resultFilePath, e);
        }
    }
}
