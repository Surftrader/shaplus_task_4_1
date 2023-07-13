package ua.com.poseal.dao;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;
import ua.com.poseal.domain.Leftover;
import ua.com.poseal.dto.LeftoverDTO;
import ua.com.poseal.util.Mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static ua.com.poseal.App.logger;

public class LeftoverDAO implements DAO<Leftover> {

    private static final String LEFTOVER = "leftover";
    private static final int BATCH_SIZE = 1000;
    private final Properties properties;
    private final Connection connection;
    private final Mapper mapper;

    private final MongoCollection<Document> mongoCollection;

    public LeftoverDAO(Properties properties) {
        this.properties = properties;
        this.connection = new MongoDBConnection();
        this.mapper = new Mapper();
        this.mongoCollection = getDocumentMongoCollection();
    }

    @Override
    public void insert(List<Leftover> leftoverList) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public List<Leftover> getAll() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public void insertLeftoverDTO(List<LeftoverDTO> dtos) {
        logger.debug("Entered insertDocument() method with leftoverDTO list = {} num", dtos.size());
        List<Document> documents = mapper.toDocuments(dtos);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        int total = documents.size();
        int batches = (int) Math.ceil((double) total / BATCH_SIZE);

        for (int i = 0; i < batches; i++) {
            int fromIndex = i * BATCH_SIZE;
            int toIndex = Math.min(fromIndex + BATCH_SIZE, total);
            List<Document> batch = documents.subList(fromIndex, toIndex);
            mongoCollection.insertMany(batch);
        }

        stopWatch.stop();
        long countDocuments = mongoCollection.countDocuments();
        logger.info("{} rows were inserted into collections {}", countDocuments, LEFTOVER);
        logger.info("RPS = {}", 1000.0 * countDocuments / stopWatch.getTime());
        logger.debug("Exited insertDocument() method");

    }

    public LeftoverDTO findAddressByCategory(String category) {
        logger.debug("Entered findAddressByCategory() method with category = {}", category);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        LeftoverDTO leftoverDTO = initLeftoverDTO(doQuery(category));

        stopWatch.stop();
        logger.info("Time finding the address = {} mc", stopWatch.getTime(TimeUnit.MILLISECONDS));
        logger.debug("Exited findAddressByCategory() method");

        return leftoverDTO;
    }

    private AggregateIterable<Document> doQuery(String category) {
        return mongoCollection.aggregate(
                Arrays.asList(new Document("$match", new Document("category", category)),
                        new Document("$group", new Document("_id", "$address")
                                .append("totalAmount", new Document("$sum", "$amount"))),
                        new Document("$sort", new Document("totalAmount", -1L)),
                        new Document("$limit", 1L)
                )
        );
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
}
