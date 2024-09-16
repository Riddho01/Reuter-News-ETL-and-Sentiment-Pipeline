import com.mongodb.client.*;
import org.bson.Document;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The {@code DBManager} class establishes the connection to the MongoDB database and provides
 * the functionality to retrieve required data from it.
 */
public class DBManger {

    //MongoDb instance
    private MongoDatabase db;


    //Name of collection containing the required data
    private String collectionName;

    /**
     * Establishes a connection to the MongoDB database using properties specified in
     * the {@code db.properties} file.
     *
     * @throws RuntimeException if there was an error encountered while creating the database
     */
    public void Connect(){
        Properties prop=new Properties();
        try(InputStream inputStream=getClass().getClassLoader().getResourceAsStream("db.properties")){

            prop.load(inputStream);
            String dbURI=prop.getProperty("db.url");
            String dbName=prop.getProperty("db.name");
            collectionName=prop.getProperty("db.collection");

            MongoClient mc= MongoClients.create(dbURI);
            db= mc.getDatabase(dbName);

        }catch(IOException e){
            throw new RuntimeException("Could not connect to database");
        }

    }

    /**
     * Retrieves values stored in the 'title' key in the documents of the specified MongoDB collection.
     *
     * @return A {@code List<String>} of titles from the database collection.
     */
    public List<String> getTitles(){
        List<String> titles=new ArrayList<>();

        MongoCollection<Document> collection=db.getCollection(collectionName);
        for(Document doc: collection.find()){
            String title= doc.getString("title");
            titles.add(title.trim().toLowerCase());
        }

        return titles;
    }

}
