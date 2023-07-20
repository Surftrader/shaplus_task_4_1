package ua.com.poseal.dao;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;
import ua.com.poseal.dto.LeftoverDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static ua.com.poseal.App.logger;

public class LeftoverDAO implements DAO<Document> {

    private static final String LEFTOVER = "leftover";
    private static final String CATEGORY_FIELD = "category";
    private static final String ADDRESS_FIELD = "address";
    private final Properties properties;
    private final Connection connection;
    private final MongoCollection<Document> mongoCollection;
    private final List<Document> aggregationQuery = Arrays.asList(null,
            new Document("$group", new Document("_id", "$address")
                    .append("totalAmount", new Document("$sum", "$amount"))),
            new Document("$sort", new Document("totalAmount", -1L)),
            new Document("$limit", 1L));

    public LeftoverDAO(Properties properties) {
        this.properties = properties;
        this.connection = new MongoDBConnection();
        this.mongoCollection = getDocumentMongoCollection();
    }

    @Override
    public void insert(List<Document> leftoverList) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public List<Document> getAll() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public void insertLeftover(List<Document> documents) {
        logger.debug("Entered insertDocument() method with Document list = {} num", documents.size());

        mongoCollection.insertMany(documents);

        logger.info("{} rows were inserted into collections \"{}\"", documents.size(), LEFTOVER);
        logger.debug("Exited insertDocument() method");
    }

    public LeftoverDTO findAddressByCategory(String category) {
        logger.debug("Entered findAddressByCategory() method with category = {}", category);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        LeftoverDTO leftoverDTO = initLeftoverDTO(doQuery(category));

        stopWatch.stop();
        logger.info("Time finding the address = {} ms", stopWatch.getTime(TimeUnit.MILLISECONDS));
        logger.debug("Exited findAddressByCategory() method");

        return leftoverDTO;
    }

    private AggregateIterable<Document> doQuery(String category) {
        aggregationQuery.set(0, new Document("$match", new Document("category", category)));
        return mongoCollection.aggregate(aggregationQuery);
    }

    private LeftoverDTO initLeftoverDTO(AggregateIterable<Document> result) {
        LeftoverDTO leftoverDTO = null;
        for (Document document : result) {
            String address = document.getString("_id");
            int totalAmount = document.getInteger("totalAmount");
            leftoverDTO = new LeftoverDTO(address, totalAmount);
        }
        return leftoverDTO;
    }

    private MongoCollection<Document> getDocumentMongoCollection() {
        MongoDatabase database = connection.getDatabase(properties);
        return database.getCollection(LEFTOVER);
    }

    public void createIndexes() {
        mongoCollection.createIndex(Indexes.ascending(CATEGORY_FIELD));
        mongoCollection.createIndex(Indexes.ascending(ADDRESS_FIELD));
    }
}
