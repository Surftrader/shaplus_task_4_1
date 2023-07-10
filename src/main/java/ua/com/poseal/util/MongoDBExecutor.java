package ua.com.poseal.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ua.com.poseal.data.Data;
import ua.com.poseal.domain.Address;
import ua.com.poseal.domain.Category;
import ua.com.poseal.domain.City;
import ua.com.poseal.domain.Store;

import java.util.ArrayList;
import java.util.Map;

public class MongoDBExecutor {
    private static final String PRODUCTS = "products";
    private static final String CATEGORIES = "categories";
    private static final String STORES = "stores";
    private static final String ADDRESSES = "addresses";
    private static final String CITIES = "cities";
    private static final String LEFTOVER = "leftover";
    private final MongoDatabase database;
    private final Data data;
    private final Mapper mapper;

    public MongoDBExecutor(MongoDatabase database) {
        this.database = database;
        this.data = new Data();
        this.mapper = new Mapper();
    }

    public void createCollections() {
        recreateCollection(PRODUCTS);
        recreateCollection(CATEGORIES);
        recreateCollection(STORES);
        recreateCollection(ADDRESSES);
        recreateCollection(CITIES);
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
        fillCollection(CITIES);
        fillCollection(ADDRESSES);
        fillCollection(STORES);
        fillCollection(CATEGORIES);
    }

    private void fillCollection(String name) {
        MongoCollection<Document> collection = database.getCollection(name);
        if (name.equalsIgnoreCase(CITIES)) {
            Map<Integer, City> cities = data.getCities();
            for (Map.Entry<Integer, City> entry : cities.entrySet()) {
                insertDocument(collection, entry.getValue());
            }
        } else if (name.equalsIgnoreCase(ADDRESSES)) {
            Map<Integer, Address> addresses = data.getAddresses();
            for (Map.Entry<Integer, Address> entry : addresses.entrySet()) {
                insertDocument(collection, entry.getValue());
            }
        } else if (name.equalsIgnoreCase(STORES)) {
            Map<Integer, Store> stores = data.getStores();
            for (Map.Entry<Integer, Store> entry : stores.entrySet()) {
                insertDocument(collection, entry.getValue());
            }
        } else if (name.equalsIgnoreCase(CATEGORIES)) {
            Map<Integer, Category> categories = data.getCategories();
            for (Map.Entry<Integer, Category> entry : categories.entrySet()) {
                insertDocument(collection, entry.getValue());
            }
        }
    }

    private void insertDocument(MongoCollection<Document> collection, Object obj) {
        Document document = mapper.objectToDocument(obj);
        collection.insertOne(document);
    }
}
