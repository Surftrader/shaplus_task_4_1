package ua.com.poseal.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import ua.com.poseal.domain.City;

import java.util.ArrayList;

public class MongoDBExecutor {

    private final MongoDatabase database;

    public MongoDBExecutor(MongoDatabase database) {
        this.database = database;
    }

    public void createCollections() {
        recreateCollection("Store");
        recreateCollection("City");
        recreateCollection("Category");
        recreateCollection("Address");
        recreateCollection("Product");
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
        fillCollection("City");
    }

    private void fillCollection(String name) {
        MongoCollection<Document> collection = database.getCollection(name);

        insertDocument(collection, new City(1L,"Київ"));
        insertDocument(collection, new City(2L,"Дніпро"));
        insertDocument(collection, new City(3L,"Львів"));
        insertDocument(collection, new City(4L, "Одеса"));
        insertDocument(collection, new City(5L,"Полтава"));
        insertDocument(collection, new City(6L,"Харків"));
    }

    private void insertDocument(MongoCollection<Document> collection, City city) {
        Document document = new Document();
        document
                .append("id", city.getId())
                .append("name", city.getName());
        collection.insertOne(document);
    }
}
