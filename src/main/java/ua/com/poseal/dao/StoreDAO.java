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

public class StoreDAO implements DAO<Document> {
    private static final String COLLECTION = "stores";
    private final MongoDatabase database;

    public StoreDAO(Properties properties) {
        Connection connection = new MongoDBConnection();
        this.database = connection.getDatabase(properties);
    }

    @Override
    public void insert(List<Document> stores) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        collection.insertMany(stores);
    }

    @Override
    public List<Document> getAll() {
        logger.debug("Entered getAll() method in StoreDAO.class");
        MongoCollection<Document> collection = database.getCollection(COLLECTION);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Document> result = collection.find().into(new LinkedList<>());

        stopWatch.stop();

        logger.info("{} stores were found in collections \"{}\" per {} ms",
                result.size(), COLLECTION, stopWatch.getTime(TimeUnit.MILLISECONDS));
        logger.debug("Exited getAll() method");

        return result;
    }
}
