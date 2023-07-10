package ua.com.poseal.service;

import ua.com.poseal.dao.DAO;
import ua.com.poseal.dao.ProductDAOImpl;
import ua.com.poseal.domain.Product;
import ua.com.poseal.util.ProductGenerator;
import ua.com.poseal.util.RowCounter;

import java.util.List;
import java.util.Properties;

public class ProductService {
    private final Properties properties;
    private final DAO<Product> productDAO;
    private final ProductGenerator productGenerator;

    public ProductService(Properties properties) {
        this.properties = properties;
        this.productDAO = new ProductDAOImpl(properties);
        this.productGenerator = new ProductGenerator();
    }

    public void saveProducts(long numbers) {
        long categories = new RowCounter(properties).countRows("categories");
        List<Product> products = productGenerator.generateProducts(numbers, (int) categories);
        productDAO.insert(products);
    }
}
