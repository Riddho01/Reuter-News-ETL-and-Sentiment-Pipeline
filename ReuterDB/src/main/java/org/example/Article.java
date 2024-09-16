package org.example;

/**
 * Represents a cleaned article ready to be inserted as a document in the MongoDb database
 *
 * This class encapsulates the attributes of an article, allowing for easy
 * retrieval of the reuter article's title and body.
 */
public class Article {

    private String title;

    public Article(String title, String body) {
        this.title = title;
        this.body = body;
    }

    private String body;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}

