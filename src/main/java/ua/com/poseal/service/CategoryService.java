package ua.com.poseal.service;

import ua.com.poseal.dao.CategoryDAO;
import ua.com.poseal.dao.DAO;
import ua.com.poseal.data.Data;
import ua.com.poseal.domain.Category;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CategoryService {
    private final DAO<Category> categoryDAO;
    private final Data data;

    public CategoryService(Properties properties) {
        this.categoryDAO = new CategoryDAO(properties);
        this.data = new Data();
    }

    public void saveCollection() {
        List<Category> categories = fillCollection();
        categoryDAO.insert(categories);
    }

    private List<Category> fillCollection() {
        List<Category> categories = new LinkedList<>();
        Map<Integer, Category> map = data.getCategories();
        for (Map.Entry<Integer, Category> entry : map.entrySet()) {
            categories.add(entry.getValue());
        }
        return categories;
    }
}
