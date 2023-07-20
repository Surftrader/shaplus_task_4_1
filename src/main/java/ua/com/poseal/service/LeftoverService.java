package ua.com.poseal.service;

import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import ua.com.poseal.dao.LeftoverDAO;
import ua.com.poseal.domain.Product;
import ua.com.poseal.domain.Store;
import ua.com.poseal.dto.LeftoverDTO;
import ua.com.poseal.util.Generator;
import ua.com.poseal.util.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static ua.com.poseal.App.logger;

public class LeftoverService {
    private static final String LEFTOVER = "leftover";
    private static final int BATCH_SIZE = 10000;
    private final Properties properties;
    private final LeftoverDAO leftoverDAO;
    private final StoreService storeService;
    private final ProductService productService;
    private final Generator generator;
    private final Mapper mapper;

    public LeftoverService(Properties properties) {
        this.properties = properties;
        this.leftoverDAO = new LeftoverDAO(properties);
        this.storeService = new StoreService(properties);
        this.productService = new ProductService(properties);
        this.generator = new Generator();
        this.mapper = new Mapper();
    }

    public void saveLeftover() {
        logger.debug("Entered saveLeftover() method");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // find stores
        List<Store> storeList = storeService.getAll();
        // find products
        List<Product> productList = productService.getAll();

        int leftoverRows = Integer.parseInt(properties.getProperty(LEFTOVER)); // 3 000 000
        int batches = leftoverRows / BATCH_SIZE; // 300
        List<Document> list = new ArrayList<>(BATCH_SIZE);
        long counter = 0;
        for (int i = 0; i < batches; i++) {
            counter = insertLeftover(BATCH_SIZE, storeList, productList, list, counter);
        }


        int rest = leftoverRows - batches * BATCH_SIZE;
        if (rest > 0) {
            insertLeftover(rest, storeList, productList, list, counter);
        }

        stopWatch.stop();

        logger.info(
                "{} rows were generated and inserted into collections \"{}\" per {} s",
                leftoverRows, LEFTOVER, stopWatch.getTime(TimeUnit.SECONDS));
        logger.info("RPS = {}", leftoverRows / stopWatch.getTime(TimeUnit.SECONDS));
        logger.debug("logger.debug(\"Entered saveLeftover() method\"); saveLeftover() method");
    }

    private long insertLeftover(int batch, List<Store> storeList, List<Product> productList, List<Document> list, long counter) {
        for (int j = 0; j < batch; j++) {
            LeftoverDTO leftoverDTO = generator.generateLeftoverDTO(storeList, productList);
            leftoverDTO.setId(++counter);
            list.add(mapper.objectToDocument(leftoverDTO));
        }
        leftoverDAO.insertLeftover(list);
        list.clear();
        return counter;
    }

    public LeftoverDTO findAddressByCategory(String category) {
        return leftoverDAO.findAddressByCategory(category);
    }

    public void createIndexes() {
        leftoverDAO.createIndexes();
    }
}
