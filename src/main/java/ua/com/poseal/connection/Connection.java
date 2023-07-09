package ua.com.poseal.connection;

import com.mongodb.client.MongoDatabase;

import java.util.Properties;

public interface Connection {
    MongoDatabase getDatabase(Properties properties);
}
