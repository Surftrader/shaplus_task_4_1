package ua.com.poseal.service;

import ua.com.poseal.dao.DAO;
import ua.com.poseal.dao.LeftoverDAO;

import java.util.Properties;

public class LeftoverService {
    private final Properties properties;
    private final DAO leftoverDAO;
    public LeftoverService(Properties properties) {
        this.properties = properties;
        this.leftoverDAO = new LeftoverDAO();
    }

    public void saveLeftover() {
    }
}
