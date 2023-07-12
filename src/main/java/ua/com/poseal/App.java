package ua.com.poseal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.poseal.service.LeftoverService;
import ua.com.poseal.service.ProductService;
import ua.com.poseal.util.Loader;
import ua.com.poseal.util.MongoDBExecutor;

import java.util.Properties;

import static ua.com.poseal.util.Loader.CATEGORY;

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

        MongoDBExecutor executor = new MongoDBExecutor(properties);
        // create tables (collections)
        executor.createCollections();
        // insert into tables (collections)
        executor.insertDataToCollections();

        // Generate products and insert them into a table
        ProductService productService = new ProductService(properties);
        productService.saveProducts(Long.parseLong(properties.getProperty(PRODUCTS)));

        // Fill leftover collection
        LeftoverService leftoverService = new LeftoverService(properties);
        leftoverService.saveLeftover();

        // query task
        logger.info("{}", leftoverService.findAddressByCategory(properties.getProperty(CATEGORY)));

        executor.close();
    }
}
