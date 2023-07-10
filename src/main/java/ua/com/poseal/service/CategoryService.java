package ua.com.poseal.service;

import ua.com.poseal.dao.CategoryDAOImpl;
import ua.com.poseal.dao.DAO;
import ua.com.poseal.domain.Category;

import java.util.List;
import java.util.Properties;

public class CategoryService {

    private final Properties properties;
    private final DAO categoryDAO;

    public CategoryService(Properties properties) {
        this.properties = properties;
        this.categoryDAO = new CategoryDAOImpl();
    }

    public void saveCategories(List<Category> categories) {

    }
}
