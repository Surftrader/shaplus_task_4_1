package ua.com.poseal;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.poseal.connection.Connection;
import ua.com.poseal.connection.MongoDBConnection;
import ua.com.poseal.service.LeftoverService;
import ua.com.poseal.service.ProductService;
import ua.com.poseal.util.CollectionCreator;
import ua.com.poseal.util.Loader;
import ua.com.poseal.util.MongoDBExecutor;
import ua.com.poseal.util.UrlBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static ua.com.poseal.connection.MongoDBConnection.DATABASE;
import static ua.com.poseal.connection.MongoDBConnection.URL;

public class App {
    public static final Logger logger = LoggerFactory.getLogger("LOGGER");
    private static final String PRODUCTS = "products";
    public static void main(String[] args) {

        try {
            App app = new App();
            app.run();
        } catch (Exception e) {
            logger.error("Error running program", e);
        }
    }

    private void run() {
        // Load properties
        Properties properties = new Loader().getFileProperties();
        Connection connection = new MongoDBConnection();
        MongoDatabase database = connection.getDatabase(properties);

        // Create a connection string with the MongoDB URI
//        UrlBuilder urlBuilder = new UrlBuilder(properties);
//        System.out.println(urlBuilder.getURL());
//        ConnectionString connectionString = new ConnectionString(properties.getProperty(URL));
//
//        // Create MongoClientSettings with the connection string
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .build();
//
//        // Create an instance of MongoClient using the settings
//        MongoClient mongoClient = MongoClients.create(settings);
//
//        MongoDatabase database = mongoClient.getDatabase(properties.getProperty(DATABASE));

//        CollectionCreator creator = new CollectionCreator(properties, database);
//        creator.createCollections();

        MongoDBExecutor executor = new MongoDBExecutor(database);
        // create tables (collections)
        executor.createCollections();
        // insert into tables (collections)
        executor.insertDataToCollections();

        // Generate products and insert them into a table
        ProductService productService = new ProductService(properties);
        productService.saveProducts(Long.parseLong(properties.getProperty(PRODUCTS)));

        // Fill leftover table
        LeftoverService leftoverService = new LeftoverService(properties);
        leftoverService.saveLeftover();

        // query task




//        // Получение доступа к коллекции
//        MongoCollection<Document> collection = database.getCollection("название_коллекции");
//        // Создание документа
//        Document document = new Document("ключ", "значение");
//        // Вставка документа в коллекцию
//        collection.insertOne(document);

    }


}
