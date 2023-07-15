package ua.com.poseal.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static ua.com.poseal.App.logger;

public class ProductDAO implements DAO<Document> {
    private static final String COLLECTION = "products";
    private final MongoDatabase database;

    public ProductDAO(Properties properties) {
        Connection connection = new MongoDBConnection();
        this.database = connection.getDatabase(properties);

    }

    @Override
    public void insert(List<Document> products) {
        logger.debug("Entered insert() method with product list = {} num", products.size());
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        collection.insertMany(products);

        stopWatch.stop();
        long rows = collection.countDocuments();
        logger.info("{} valid products were inserted into collections \"{}\" per {} ms",
                rows, COLLECTION, stopWatch.getTime(TimeUnit.MILLISECONDS));
        logger.debug("Exited insert() method");
    }

    @Override
    public List<Document> getAll() {
        logger.debug("Entered getAll() method in ProductDAO.class");
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Document> result = collection.find().into(new LinkedList<>());

        stopWatch.stop();
        logger.info("{} products were found in {} collection", result.size(), COLLECTION);
        logger.debug("Exited getAll() method");

        return result;
    }
}
