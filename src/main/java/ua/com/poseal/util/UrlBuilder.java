package ua.com.poseal.util;

import java.util.Properties;

public class UrlBuilder {

    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PORT = "port";

    private final Properties properties;

    public UrlBuilder(Properties properties) {
        this.properties = properties;
    }

    public String getURL(){
        // "mongodb://username:password@localhost:27017"
        StringBuilder sb = new StringBuilder();
        sb.append("mongodb://")
                .append(properties.getProperty(USERNAME))
                .append(":")
                .append(properties.getProperty(PASSWORD))
                .append("@")
                .append("localhost")
                .append(":")
                .append(properties.getProperty(PORT));
        return sb.toString();
    }
}
