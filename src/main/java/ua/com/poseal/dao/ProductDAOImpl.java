package ua.com.poseal.dao;

import com.mongodb.client.MongoDatabase;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;
import ua.com.poseal.domain.Product;

import javax.swing.text.Document;
import java.util.List;
import java.util.Properties;

public class ProductDAOImpl implements DAO<Product> {

    private static final String COLLECTION = "Products";
    private final Properties properties;
    private final Connection connection;

    public ProductDAOImpl(Properties properties) {
        this.properties = properties;
        this.connection = new MongoDBConnection();
    }

    @Override
    public void insert(List<Product> products) {
        MongoDatabase database = connection.getDatabase(properties);
        List<Document> documents;
        
        System.out.println("Method insertProducts from ProductDAO");

    }
}
