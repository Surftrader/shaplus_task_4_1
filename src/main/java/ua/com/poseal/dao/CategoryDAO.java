package ua.com.poseal.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;
import ua.com.poseal.domain.Category;
import ua.com.poseal.util.Mapper;

import java.util.List;
import java.util.Properties;

public class CategoryDAO implements DAO<Category> {

    private static final String COLLECTION = "categories";
    private final Connection connection;
    private final MongoDatabase database;

    private final Mapper mapper;

    public CategoryDAO(Properties properties) {
        this.connection = new MongoDBConnection();
        this.database = connection.getDatabase(properties);
        this.mapper = new Mapper();
    }

    @Override
    public void insert(List<Category> categories) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        categories.forEach(s -> insertDocument(collection, s));
    }

    @Override
    public List<Category> getAll() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    private void insertDocument(MongoCollection<Document> collection, Category category) {
        Document document = mapper.objectToDocument(category);
        collection.insertOne(document);
    }
}
