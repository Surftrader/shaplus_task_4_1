package ua.com.poseal.connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Properties;

import static ua.com.poseal.App.logger;

public class MongoDBConnection implements Connection {
    public static final String URL = "url";
    public static final String DATABASE = "database";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final MongoDBConnection instance = getInstance();

    private MongoClient mongoClient;

    public static synchronized MongoDBConnection getInstance() {
        if (instance == null) {
            return new MongoDBConnection();
        }
        return instance;
    }

    @Override
    public MongoDatabase getDatabase(Properties properties) {
        // Creating a MongoDB Client
        MongoDatabase mongoDatabase = null;
        try {

            String template = "mongodb://%s:%s@%s/%s?%s";
            String username = properties.getProperty(USERNAME);
            String password = properties.getProperty(PASSWORD);
            String clusterEndpoint = properties.getProperty(URL);
            String database = properties.getProperty(DATABASE);
            String readPreference = "ssl=true&&ssl_ca_certs=/tmp/certs/global-bundle.pem&replicaSet=rs0&readpreference=secondaryPreferred";

            String connectionString = String.format(
                    template, username, password, clusterEndpoint, database, readPreference);
//            String truststore = properties.getProperty("javax.net.ssl.trustStore");
//            String truststorePassword = properties.getProperty("javax.net.ssl.trustStorePassword");
//            System.setProperty("javax.net.ssl.trustStore", truststore);
//            System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

            mongoClient = MongoClients.create(connectionString);
            mongoDatabase = mongoClient.getDatabase(properties.getProperty(DATABASE));

        } catch (Exception e) {
            logger.error("Error connection to mongoDatabase {}", properties.getProperty(DATABASE));
        }
        return mongoDatabase;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
