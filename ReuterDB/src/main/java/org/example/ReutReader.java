package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code ReutReader} class concerns reading from the {@code reut2-009.sgm} file.
 * It extracts the titles and bodies of articles by parsing corresponding tags, cleans the unwanted expressions
 * as in the {@code remove} attribute and tags, and stores them as {@link Article} objects.
 */
public class ReutReader {

    //Path of the sgm router file
    private final String reuterPath;

    //Regex to remove unwanted expressions from Reuter title and body
    private String remove="&lt;|>|&#[0-9]+;";


    public List<Article> getArticles() {

        return articles;
    }

    //List of Article objects to be inserted into MongoDb database
    private List<Article> articles = new ArrayList<>();

    /**
     * Constructor to initialize the path of the {@code reut2-009.sgm} file.
     */
    public ReutReader() {
        this.reuterPath = getFilePath();
    }

    /**
     * Retrieve the file path of the {@code reut2-009.sgm} file from the {@code sgmpath.properties} file.
     *
     * @return The path of the file as a {@code String}.
     * @throws RuntimeException if properties file could not be found.
     */
    private String getFilePath(){
        Properties prop=new Properties();
        try(InputStream inputStream=getClass().getClassLoader().getResourceAsStream("sgmpath.properties")){
            prop.load(inputStream);
            return prop.getProperty("sgm.path");
        } catch (IOException e) {
            throw new RuntimeException("Could not load the reuter file");
        }
    }

    /**
     * This method parses the data in the {@code reut2-009.sgm} file, cleans the articles by removing
     * unwanted expressions and tags, and stores the results as {@link Article} objects in the {@code articles} list.
     *
     * @throws RuntimeException if an error is encountered while reading the {@code reut2-009.sgm} file.
     */
    public void cleanFile(){
        //Regex patterns for each tag
        Pattern titleTag=Pattern.compile("<TITLE>(.*?)</TITLE>",Pattern.DOTALL);
        Pattern bodyTag=Pattern.compile("<BODY>(.*?)</BODY>",Pattern.DOTALL);

        //Read file contents from file path
        try(InputStream inputStream=getClass().getClassLoader().getResourceAsStream(reuterPath);
            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream))) {

            //Read and store each line in the file
            StringBuilder articleData = new StringBuilder();
            String line;
            boolean insideArticle = false;
            while ((line = br.readLine()) != null) {


                //Check if data inside a <Reuters> tag is being read
                if (line.contains("<REUTERS")) {
                    insideArticle = true;
                }

                if (insideArticle) {
                    //Get all content inside <Reuters> tag
                    articleData.append(line).append("\n");
                }

                if (line.contains("</REUTERS>")) {
                    insideArticle = false;


                    Matcher titleMatcher = titleTag.matcher(articleData);
                    Matcher bodyMatcher = bodyTag.matcher(articleData);

                    //Default Empty Title
                    String title = "";
                    if (titleMatcher.find()) {
                        title = titleMatcher.group(1).replaceAll(remove, "").trim();
                    }

                    //Default Empty Body
                    String body = "";
                    if (bodyMatcher.find()) {
                        body = bodyMatcher.group(1).replaceAll(remove, "").replaceAll("\\s+", " ").trim();

                        //Remove Additional Reuter text at the end of body
                        body = body.replaceAll("(?i)\\s*Reuter\\s*$", "");

                    }
                    articles.add(new Article(title, body));

                    articleData.setLength(0);
                }
            }
        }catch (IOException e){
            throw new RuntimeException("An error occurred while reading the reuter file");
        }
    }

}
