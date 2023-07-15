package ua.com.poseal.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;

import java.util.List;
import java.util.Properties;

import static ua.com.poseal.App.logger;

public class CategoryDAO implements DAO<Document> {
    private static final String COLLECTION = "categories";
    private final MongoDatabase database;

    public CategoryDAO(Properties properties) {
        Connection connection = new MongoDBConnection();
        this.database = connection.getDatabase(properties);
    }

    @Override
    public void insert(List<Document> categories) {
        logger.debug("Entered insert() method with category list size {}", categories.size());

        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        collection.insertMany(categories);

        logger.debug("Exited insert() method");
    }

    @Override
    public List<Document> getAll() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

}
