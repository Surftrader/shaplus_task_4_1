package ua.com.poseal.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;
import ua.com.poseal.domain.Store;
import ua.com.poseal.util.Mapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static ua.com.poseal.App.logger;

public class StoreDAO implements DAO<Store> {
    private static final String COLLECTION = "stores";
    private final MongoDatabase database;
    private final Mapper mapper;

    public StoreDAO(Properties properties) {
        Connection connection = new MongoDBConnection();
        this.database = connection.getDatabase(properties);
        this.mapper = new Mapper();
    }

    @Override
    public void insert(List<Store> stores) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        stores.forEach(s -> insertDocument(collection, s));
    }

    @Override
    public List<Store> getAll() {
        logger.debug("Entered getAll() method");
        MongoCollection<Document> collection = database.getCollection(COLLECTION);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        FindIterable<Document> documents = collection.find();

        stopWatch.stop();

        List<Store> result = new LinkedList<>();
        for (Document doc : documents) {
            result.add(mapper.documentToStore(doc));
        }
        logger.info("{} stores were found in collections {}", result.size(), COLLECTION);
        logger.info("RPS = {}", 1000.0 * result.size() / stopWatch.getTime());
        logger.debug("Exited getAll() method");

        return result;
    }

    private void insertDocument(MongoCollection<Document> collection, Store store) {
        collection.insertOne(mapper.objectToDocument(store));
    }
}
