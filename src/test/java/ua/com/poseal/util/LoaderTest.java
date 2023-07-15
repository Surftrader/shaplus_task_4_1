package ua.com.poseal.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static ua.com.poseal.connection.MongoDBConnection.DATABASE;

class LoaderTest {
    private static final String PRODUCTS = "products";
    private static final String LEFTOVER = "leftover";
    Loader loader;

    @BeforeEach
    void init() {
        loader = new Loader();
    }

    @Test
    void getExternalFileProperties() {
        Properties properties = loader.getFileProperties();

        assertNotNull(properties);
        assertEquals("shapp_db", properties.getProperty(DATABASE));
        assertEquals(2000, Integer.parseInt(properties.getProperty(PRODUCTS)));
        assertEquals(3000000, Integer.parseInt(properties.getProperty(LEFTOVER)));
    }

    @Test
    void getInternalFileProperties() {
        Properties properties = new Properties();
        loader.downloadInternalProperties(properties);

        assertNotNull(properties);
        assertEquals("shapp_db", properties.getProperty(DATABASE));
        assertEquals(2000, Integer.parseInt(properties.getProperty(PRODUCTS)));
        assertEquals(3000000, Integer.parseInt(properties.getProperty(LEFTOVER)));
    }
}
