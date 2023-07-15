package ua.com.poseal.connection;

import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.poseal.util.Loader;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ua.com.poseal.connection.MongoDBConnection.DATABASE;

class MongoDBConnectionTest {

    Properties properties;
    Connection connection;

    @BeforeEach
    void init() {
        properties = new Loader().getFileProperties();
        connection = new MongoDBConnection();
    }

    @Test
    void getDatabase() {
        MongoDatabase database = connection.getDatabase(properties);
        String databaseName = database.getName();

        assertNotNull(database);
        assertEquals(properties.getProperty(DATABASE), databaseName);

        connection.close();
    }

    @Test
    void getInstance() {
        int first = MongoDBConnection.instance.hashCode();
        int second = MongoDBConnection.getInstance().hashCode();

        assertEquals(first, second);
    }
}