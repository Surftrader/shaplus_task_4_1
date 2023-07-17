package ua.com.poseal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.poseal.dto.LeftoverDTO;
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
        logger.debug("Start program");
        try {
            App app = new App();
            app.run();
        } catch (Exception e) {
            logger.error("Error running program", e);
        }
        logger.debug("Stop program");
    }

    private void run() {
        logger.debug("Entered run() method");
        // Load properties
        Properties properties = new Loader().getFileProperties();

        MongoDBExecutor executor = new MongoDBExecutor(properties);
        executor.createCollections();
        executor.insertDataToCollections();
        // Generate products and insert them in the collection
        executor.saveProducts(Long.parseLong(properties.getProperty(PRODUCTS)));
        // Fill leftover collection
        executor.saveLeftover();
        // query task
        LeftoverDTO dto = executor.findAddressByCategory(properties.getProperty(CATEGORY));
        logger.info(
                "The address of the store with the largest number of products in the category \"{}\": {}, count = {}",
                properties.getProperty(CATEGORY), dto.getAddress(), dto.getAmount());

        executor.close();

        logger.debug("Exited run() method");
    }
}
