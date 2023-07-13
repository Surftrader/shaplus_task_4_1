package ua.com.poseal.dao;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneModel;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;
import ua.com.poseal.domain.Product;
import ua.com.poseal.util.Mapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import static ua.com.poseal.App.logger;

public class ProductDAO implements DAO<Product> {

    private static final int BATCH_SIZE = 500;
    private static final String COLLECTION = "products";
    private final MongoDatabase database;
    private final Mapper mapper;

    public ProductDAO(Properties properties) {
        Connection connection = new MongoDBConnection();
        this.database = connection.getDatabase(properties);
        this.mapper = new Mapper();
    }

    @Override
    public void insert(List<Product> products) {
        logger.debug("Entered insert() method with product list = {} num", products.size());
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        insertBatch(products, collection);

        stopWatch.stop();
        long rows = collection.countDocuments();
        logger.info("{} valid products were inserted into collections {}", rows, COLLECTION);
        logger.info("RPS = {}", 1000.0 * rows / stopWatch.getTime());
        logger.debug("Exited insert() method");
    }

    @Override
    public List<Product> getAll() {
        logger.debug("Entered getAll() method");
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        FindIterable<Document> documents = collection.find();

        stopWatch.stop();
        List<Product> result = new LinkedList<>();
        for (Document doc : documents) {
            result.add(mapper.documentToProduct(doc));
        }
        logger.info("{} products were found in {} collection", result.size(), COLLECTION);
        logger.debug("Exited getAll() method");

        return result;
    }

    private void insertBatch(List<Product> products, MongoCollection<Document> collection) {
        AtomicLong count = new AtomicLong(1);
        List<InsertOneModel<Document>> inserts = new LinkedList<>();
        products.forEach(s -> {
                    inserts.add(new InsertOneModel<>(mapper.objectToDocument(s)));
                    int i = products.indexOf(s);
                    if (i > 0 && i % BATCH_SIZE == 0) {
                        BulkWriteResult result = collection.bulkWrite(inserts);
                        count.addAndGet(result.getInsertedCount());
                        inserts.clear();
                    }
                }
        );
        if (!inserts.isEmpty()) {
            BulkWriteResult result = collection.bulkWrite(inserts);
            count.addAndGet(result.getInsertedCount());
            inserts.clear();
        }
    }
}
