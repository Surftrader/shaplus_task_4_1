package ua.com.poseal.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;
import ua.com.poseal.dto.LeftoverDTO;
import ua.com.poseal.service.CategoryService;
import ua.com.poseal.service.LeftoverService;
import ua.com.poseal.service.ProductService;
import ua.com.poseal.service.StoreService;

import java.util.ArrayList;
import java.util.Properties;

public class MongoDBExecutor {
    private static final String PRODUCTS = "products";
    private static final String CATEGORIES = "categories";
    private static final String STORES = "stores";
    private static final String LEFTOVER = "leftover";
    private final Connection connection;
    private final MongoDatabase database;
    private final StoreService storeService;
    private final CategoryService categoryService;

    private final ProductService productService;
    private final LeftoverService leftoverService;


    public MongoDBExecutor(Properties properties) {
        this.connection = new MongoDBConnection();
        this.database = MongoDBConnection.getInstance().getDatabase(properties);
        this.storeService = new StoreService(properties);
        this.categoryService = new CategoryService(properties);
        this.productService = new ProductService(properties);
        this.leftoverService = new LeftoverService(properties);
    }

    public void createCollections() {
        recreateCollection(PRODUCTS);
        recreateCollection(CATEGORIES);
        recreateCollection(STORES);
        recreateCollection(LEFTOVER);
    }

    private void recreateCollection(String name) {
        // Access the collection
        MongoCollection<Document> collection = database.getCollection(name);
        // Check if collection exists
        if (database.listCollectionNames().into(new ArrayList<>()).contains(name)) {
            collection.drop();
        }
        // Create new collection
        database.createCollection(name);
    }

    public void insertDataToCollections() {
        storeService.saveCollection();
        categoryService.saveCollection();
    }

    public void close() {
        if (connection != null) {
            connection.close();
        }
    }

    public void saveProducts(long products) {
        productService.saveProducts(products);
    }

    public void saveLeftover() {
        leftoverService.saveLeftover();
    }

    public LeftoverDTO findAddressByCategory(String category) {
        return leftoverService.findAddressByCategory(category);
    }
}
