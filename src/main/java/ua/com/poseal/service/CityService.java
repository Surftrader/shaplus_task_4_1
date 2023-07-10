package ua.com.poseal.service;

import ua.com.poseal.dao.CityDAOImpl;
import ua.com.poseal.dao.DAO;

import java.util.Properties;

public class CityService {

    private final Properties properties;
    private final DAO cityDAO;

    public CityService(Properties properties) {
        this.properties = properties;
        this.cityDAO = new CityDAOImpl();
    }
}
