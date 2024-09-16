package org.example;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Manages connection to a MongoDB database and provides functionality to write to it once
 * reuter data has been cleaned.
 */
public class ReuterDbManager {

    //The MongoDb database object
    private MongoDatabase db;

    private String collectionName;

/** Constructor to initialize a new {@code ReuterDbManager} object which initiates a connection to the database.
 *
 * Calls the {@code Connect()} method to establish a database connection
 */
    public ReuterDbManager(){

        Connect();
    }

    /**
     * Establishes a connection to a Mongodb Database by reading the configuration from the
     * @code dbconfig.properties file
     *
     *The method reads the db.url, dn.name and db.collection properties and sets up a connection
     * with database with those credentials
     *
     * In case the properties file cannot be read or connection cannot be established a @code is thrown
     *
     * @throws RuntimeException is throwm if database connection cannot be established if file cannot be read
     */
    private void Connect(){

        Properties prop=new Properties();

        try(InputStream inputStream=getClass().getClassLoader().getResourceAsStream("dbconfig.properties")){

            prop.load(inputStream);

            String dbUri=prop.getProperty("db.url");
            String dbName=prop.getProperty("db.name");
            collectionName= prop.getProperty("db.collection");

            MongoClient mc= MongoClients.create(dbUri);
            db=mc.getDatabase(dbName);
        } catch (IOException ex) {
            throw new RuntimeException("Could not connect to database");
        }
    }

    /**
     * Insert an article as a document into a MongoDb Collection
     *
     *This method takes an {@code Article} object as a parameter and inserts it into the MongoDB collection with name as
     * that of the {@code collectionName} attribute. The article is converted into a {@code Document} object before
     * insertion into the database. This method assumes that the {@code Article} object contains title and body attributes
     * that are inserted as fields in the document.
     *
     * @param article The {@code Article} object that has to be inserted into the MongoDb database
     */
    public void insertArticle(Article article){
        MongoCollection<Document> coll= db.getCollection(collectionName);
        Document d=new Document("title",article.getTitle()).append("body",article.getBody());
        coll.insertOne(d);
    }
}
