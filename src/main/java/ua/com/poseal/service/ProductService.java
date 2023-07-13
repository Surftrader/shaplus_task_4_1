package ua.com.poseal.service;

import ua.com.poseal.dao.DAO;
import ua.com.poseal.dao.ProductDAO;
import ua.com.poseal.domain.Product;
import ua.com.poseal.util.Generator;
import ua.com.poseal.util.RowCounter;

import java.util.List;
import java.util.Properties;

public class ProductService {
    private static final String COLLECTION = "categories";
    private final Properties properties;
    private final DAO<Product> productDAO;
    private final Generator generator;

    public ProductService(Properties properties) {
        this.properties = properties;
        this.productDAO = new ProductDAO(properties);
        this.generator = new Generator(properties);
    }

    public void saveProducts(long numbers) {
        long categories = new RowCounter(properties).countRows(COLLECTION);
        List<Product> products = generator.generateProducts(numbers, (int) categories);
        productDAO.insert(products);
    }

    public List<Product> getAll() {
        return productDAO.getAll();
    }
}
