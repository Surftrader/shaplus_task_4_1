package ua.com.poseal.dao;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneModel;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;
import ua.com.poseal.domain.Leftover;
import ua.com.poseal.dto.LeftoverDTO;
import ua.com.poseal.util.Mapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import static ua.com.poseal.App.logger;

public class LeftoverDAO implements DAO<Leftover> {

    private static final String COLLECTION = "leftover";
    private final Properties properties;
    private final Connection connection;
    private final Mapper mapper;

    public LeftoverDAO(Properties properties) {
        this.properties = properties;
        this.connection = new MongoDBConnection();
        this.mapper = new Mapper();
    }

    @Override
    public void insert(List<Leftover> leftoverList) {
        logger.debug("Entered insert() method with leftover list = {} num", leftoverList.size());
        MongoDatabase database = connection.getDatabase(properties);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

//        int batchSize = 500;
//        insertBatch(leftoverList, collection, batchSize);
//        int total = leftoverList.size();
//        int batches = (int) Math.ceil((double) total / batchSize);
//
//        for (int i = 0; i < batches; i++) {
//            int fromIndex = i * batchSize;
//            int toIndex = Math.min(fromIndex + batchSize, total);
//
//            List<Document> documents = new LinkedList<>();
//            IntStream.range(fromIndex, toIndex).forEach(j -> documents.add(mapper.objectToDocument(leftoverList.get(j))));
//            collection.insertMany(documents);
//        }

        AtomicLong count = new AtomicLong(1);
        int batchSize = 1000;
        List<InsertOneModel<Document>> inserts = new LinkedList<>();

        leftoverList.forEach(s -> {
                    inserts.add(new InsertOneModel<>(mapper.objectToDocument(s)));
                    int i = leftoverList.indexOf(s);
                    if (i > 0 && i % batchSize == 0) {
                        BulkWriteResult result = collection.bulkWrite(inserts);
                        count.addAndGet(result.getInsertedCount());
                        inserts.clear();
                    }
                }
        );

//        for (int i = 0; i < leftoverList.size(); i++) {
//            inserts.add(new InsertOneModel<>(mapper.objectToDocument(leftoverList.get(i))));
//            if (i > 0 && i % batchSize == 0) {
//                BulkWriteResult result = collection.bulkWrite(inserts);
//                count.addAndGet(result.getInsertedCount());
//                inserts.clear();
//            }
//        }

        if (!inserts.isEmpty()) {
            BulkWriteResult result = collection.bulkWrite(inserts);
            count.addAndGet(result.getInsertedCount());
            inserts.clear();
        }

        stopWatch.stop();

        logger.info("{} leftovers were inserted into collections {}", count, COLLECTION);
        logger.info("RPS = {}", 1000.0 * count.get() / stopWatch.getTime());
        logger.debug("Exited insert() method");
    }

//    private void insertBatch(List<Leftover> leftoverList, MongoCollection<Document> collection, int batchSize) {
//        int total = leftoverList.size();
//        int batches = (int) Math.ceil((double) total / batchSize);
//
//        for (int i = 0; i < batches; i++) {
//            int fromIndex = i * batchSize;
//            int toIndex = Math.min(fromIndex + batchSize, total);
//
//            List<Document> documents = new LinkedList<>();
//            IntStream.range(fromIndex, toIndex).forEach(j -> documents.add(mapper.objectToDocument(leftoverList.get(j))));
//            collection.insertMany(documents);
//        }
//    }

    @Override
    public List<Leftover> getAll() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public void insertDTO(List<LeftoverDTO> dtos) {
        logger.debug("Entered insertDTO() method with leftoverDTO list = {} num", dtos.size());
        MongoDatabase database = connection.getDatabase(properties);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        AtomicLong count = new AtomicLong(1);
        int batchSize = 1000;
        List<InsertOneModel<Document>> inserts = new LinkedList<>();

        dtos.forEach(s -> {
                    inserts.add(new InsertOneModel<>(mapper.objectToDocument(s)));
                    int i = dtos.indexOf(s);
                    if (i > 0 && i % batchSize == 0) {
                        BulkWriteResult result = collection.bulkWrite(inserts);
                        count.addAndGet(result.getInsertedCount());
//                        logger.info("{} leftovers were saved", count);
                        inserts.clear();
                    }
                }
        );

        if (!inserts.isEmpty()) {
            BulkWriteResult result = collection.bulkWrite(inserts);
            count.addAndGet(result.getInsertedCount());
            inserts.clear();
        }

        stopWatch.stop();

        logger.info("{} leftovers were inserted into collections {}", count, COLLECTION);
        logger.info("RPS = {}", 1000.0 * count.get() / stopWatch.getTime());
        logger.debug("Exited insertDTO() method");
    }

    public LeftoverDTO findAddressByCategory(String category) {
        // TODO: aggregate function

        return null;
    }
}
