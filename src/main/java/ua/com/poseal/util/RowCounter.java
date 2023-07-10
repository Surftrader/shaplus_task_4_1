package ua.com.poseal.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;

import java.util.Properties;

import static ua.com.poseal.App.logger;

public class RowCounter {
    private final Properties properties;
    private final Connection connection;

    public RowCounter(Properties properties) {
        this.properties = properties;
        this.connection = new MongoDBConnection();
    }

    public Long countRows(String collection) {
        logger.debug("Entered countRows() method with parameter table={}", collection);
        MongoDatabase database = connection.getDatabase(properties);

        MongoCollection<Document> collections = database.getCollection(collection);
        Long result = collections.countDocuments();

        logger.info("Count from {} = {}", collection, result);
        logger.debug("Exited countRows() method");
        return result;
    }
}
