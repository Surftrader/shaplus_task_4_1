package ua.com.poseal.connection;

import com.mongodb.ConnectionString;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Properties;

import static ua.com.poseal.App.logger;

public class MongoDBConnection implements Connection {
    public static final String URL = "url";
    public static final String DATABASE = "database";
    public static final MongoDBConnection instance = getInstance();

    public static synchronized MongoDBConnection getInstance() {
        if (instance == null) {
            return new MongoDBConnection();
        }
        return instance;
    }

    @Override
    public MongoDatabase getDatabase(Properties properties) {
        // Creating a MongoDB Client
        MongoDatabase database = null;
        try {
//            MongoClient mongoClient = MongoClients.create(properties.getProperty(URL));
//            // Getting access to the database
//            database = mongoClient.getDatabase(properties.getProperty(DATABASE));
            ConnectionString connectionString = new ConnectionString(properties.getProperty(URL));

            // Create MongoClientSettings with the connection string
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            // Create an instance of MongoClient using the settings
            MongoClient mongoClient = MongoClients.create(settings);

            database = mongoClient.getDatabase(properties.getProperty(DATABASE));

        } catch (Exception e) {
            logger.error("Error connection to database {}", properties.getProperty(DATABASE));
        }
        return database;
    }
}
