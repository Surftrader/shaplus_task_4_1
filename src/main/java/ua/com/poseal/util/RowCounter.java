package ua.com.poseal.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;

import java.util.Properties;

import static ua.com.poseal.App.logger;

public class RowCounter {
    private final MongoDatabase database;

    public RowCounter(Properties properties) {
        Connection connection = new MongoDBConnection();
        this.database = connection.getDatabase(properties);
    }

    public Long countRows(String collection) {
        logger.debug("Entered countRows() method with parameter table={}", collection);

        MongoCollection<Document> collections = database.getCollection(collection);
        Long result = collections.countDocuments();

        logger.info("Count from {} = {}", collection, result);
        logger.debug("Exited countRows() method");
        return result;
    }
}
