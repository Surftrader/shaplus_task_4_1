package ua.com.poseal.service;

import org.bson.Document;
import ua.com.poseal.dao.CategoryDAO;
import ua.com.poseal.dao.DAO;
import ua.com.poseal.data.Data;
import ua.com.poseal.domain.Category;
import ua.com.poseal.util.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class CategoryService {
    private final DAO<Document> categoryDAO;
    private final Data data;
    private final Mapper mapper;

    public CategoryService(Properties properties) {
        this.categoryDAO = new CategoryDAO(properties);
        this.data = new Data();
        this.mapper = new Mapper();
    }

    public void saveCollection() {
        List<Category> categories = fillCollection();
        List<Document> collect = categories.stream()
                .map(mapper::objectToDocument)
                .collect(Collectors.toList());
        categoryDAO.insert(collect);
    }

    private List<Category> fillCollection() {
        Map<Integer, Category> map = data.getCategories();
        return new ArrayList<>(map.values());
    }
}
