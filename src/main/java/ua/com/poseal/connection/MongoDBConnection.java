package ua.com.poseal.connection;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import javax.print.attribute.standard.JobOriginatingUserName;
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
        MongoDatabase database = null;
        try {
//            ConnectionString connectionString = new ConnectionString(properties.getProperty(URL));

            //:27017/?ssl=true&ssl_ca_certs=/config/global-bundle.pem&retryWrites=false

            String template = "mongodb://%s:%s@%s/shapp_db?%s";
            String username = properties.getProperty(USERNAME);
            String password = properties.getProperty(PASSWORD);
            String clusterEndpoint = "docdb-2023-07-17-17-11-32.cqqiakrcxslc.eu-west-3.docdb.amazonaws.com:27017";
//            String readPreference = "ssl=true&ssl_ca_certs=/config/global-bundle.pem&retryWrites=false&javax.net.ssl.trustStore=/home/ec2-user/rds-truststore.jks&javax.net.ssl.trustStorePassword=storepassword";
            String readPreference = "ssl=true&ssl_ca_certs=/config/global-bundle.pem&retryWrites=false&javax.net.ssl.trustStore=/home/ec2-user/rds-truststore.jks&javax.net.ssl.trustStorePassword=storepassword";
            String connectionString = String.format(template, username, password, clusterEndpoint, readPreference);

            String truststore = properties.getProperty("javax.net.ssl.trustStore");
            String truststorePassword = properties.getProperty("javax.net.ssl.trustStorePassword");

            System.setProperty("javax.net.ssl.trustStore", truststore);
            System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

            MongoClient mongoClient = MongoClients.create(connectionString);



//            MongoClientSettings settings = MongoClientSettings.builder()
//                    .applyConnectionString(connectionString)
//                    .build();
//            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(properties.getProperty(DATABASE));

        } catch (Exception e) {
            logger.error("Error connection to database {}", properties.getProperty(DATABASE));
        }
        return database;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
