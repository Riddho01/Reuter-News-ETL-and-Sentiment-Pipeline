package org.example;
/**
 * The Main class which acts as the entry point of the application.
 *
 * It follows the steps of reading from the {@code reut2-009.sgm} file, cleaning and extracting the relevant data,
 * and then inserting the data as documents into a MongoDb collection
 */
public class Main {

    public static void main(String[] args) {
        //Instantiate a ReutReader to read from the Reuters file and perform cleaning
        ReutReader rr = new ReutReader();
        rr.cleanFile();

        //Instantiate a ReuterDbManager to manage operations of the MongoDb database
        ReuterDbManager dbManager = new ReuterDbManager ();

        //Insert each cleaned article into the database
        for (Article article : rr.getArticles()) {
            dbManager.insertArticle(article);
        }
    }

}
