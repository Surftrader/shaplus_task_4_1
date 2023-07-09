package ua.com.poseal.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import static ua.com.poseal.App.logger;

public class Loader {

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
}
