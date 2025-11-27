package ua.com.poseal.util;

import com.opencsv.CSVReader;
import ua.com.poseal.data.Data;
import ua.com.poseal.domain.Address;
import ua.com.poseal.domain.Category;
import ua.com.poseal.domain.City;
import ua.com.poseal.domain.Store;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ua.com.poseal.App.logger;

public class Loader {

    public static final String CATEGORIES_FILE = "categories.csv";
    public static final String STORES_FILE = "stores.csv";
    public static final String PROPERTY_FILE = "application.properties";
    public static final String PROPERTY_FOLDER = "config";
    public static final String CATEGORY = "category";
    private static final String DEFAULT_CATEGORY = "Продукти";

    public Properties getFileProperties() {
        logger.debug("Entered getFileProperties() method");
        Properties properties = new Properties();
        String path = PROPERTY_FOLDER + File.separator + PROPERTY_FILE;
        File file = new File(path);
        logger.info("File was created by path={}", path);

        if (file.exists()) {
            downloadExternalProperties(properties, file);
        } else {
            downloadInternalProperties(properties);
        }
        downloadSystemProperties(properties);
        logger.debug("Exited getFileProperties() method");
        return properties;
    }

    protected void downloadExternalProperties(Properties properties, File file) {
        logger.debug("Entered downloadExternalProperties() method with arguments: properties={}, file={}", properties, file.getName());
        try (InputStream is = new FileInputStream(file);
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            properties.load(reader);
            logger.info("Properties were downloaded from file={}", file.getName());
        } catch (IOException e) {
            logger.error("Error loading properties from external file", e);
        }
        logger.debug("Exited downloadExternalProperties() method");
    }

    protected void downloadInternalProperties(Properties properties) {
        logger.debug("Entered downloadInternalProperties() method with argument: properties={}", properties);
        try (InputStream is = Loader.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
             Reader reader = new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            logger.error("Error loading properties from internal file", e);
        }
        logger.debug("Exited downloadInternalProperties() method");
    }

    protected void downloadSystemProperties(Properties properties) {
        logger.debug("Entered downloadSystemProperties() method");
        Properties props = System.getProperties();
        properties.putAll(props);
        String category = Optional.ofNullable(properties.getProperty(CATEGORY))
                .orElse(DEFAULT_CATEGORY);
        properties.setProperty(CATEGORY, category);
        logger.info("The \"{}\" category was obtained from the properties", category);
        logger.debug("Exited downloadSystemProperties() method");
    }

    public Data loadDataFromFiles() {
        Data data = new Data();
        data.setCategories(loadCategories());
        data.setStores(loadStores());
        return data;
    }

    private Map<Integer, Store> loadStores() {
        logger.debug("Entered loadStores() method");
        Map<Integer, Store> map = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(
                PROPERTY_FOLDER + File.separator + STORES_FILE))) {
            String[] line;
            int key = 0;
            while ((line = reader.readNext()) != null) {
                map.put(++key, new Store(Long.parseLong(line[0]), line[1],
                        new Address(Long.parseLong(line[2]), line[3],
                                new City(Long.parseLong(line[4]), line[5]))));
            }
        } catch (Exception ex) {
            logger.error("Error reading file {}", STORES_FILE);
        }
        logger.info("{} stores were loaded from file {}", map.size(), STORES_FILE);
        logger.debug("Exited loadStores() method");
        return map;
    }

    private Map<Integer, Category> loadCategories() {
        logger.debug("Entered loadCategories() method");
        Map<Integer, Category> map = new HashMap<>();
        try (CSVReader reader = new CSVReader(
                new FileReader(PROPERTY_FOLDER + File.separator + CATEGORIES_FILE))) {
            String[] line;
            int key = 0;
            while ((line = reader.readNext()) != null) {
                map.put(++key, new Category(Long.parseLong(line[0]), line[1]));
            }
        } catch (Exception ex) {
            logger.error("Error reading file {}", CATEGORIES_FILE);
        }
        logger.info("{} categories were loaded from file {}", map.size(), CATEGORIES_FILE);
        logger.debug("Exited loadCategories() method");
        return map;
    }
}
