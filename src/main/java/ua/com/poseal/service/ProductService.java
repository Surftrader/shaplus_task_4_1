package ua.com.poseal.service;

import org.bson.Document;
import ua.com.poseal.dao.DAO;
import ua.com.poseal.dao.ProductDAO;
import ua.com.poseal.domain.Product;
import ua.com.poseal.util.Generator;
import ua.com.poseal.util.Mapper;
import ua.com.poseal.util.RowCounter;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ProductService {
    private static final String COLLECTION = "categories";
    private final Properties properties;
    private final DAO<Document> productDAO;
    private final Generator generator;
    private final Mapper mapper;

    public ProductService(Properties properties) {
        this.properties = properties;
        this.productDAO = new ProductDAO(properties);
        this.generator = new Generator(properties);
        this.mapper = new Mapper();
    }

    public void saveProducts(long numbers) {
        long categories = new RowCounter(properties).countRows(COLLECTION);
        List<Product> products = generator.generateProducts(numbers, (int) categories);
        List<Document> collect = products
                .stream()
                .map(mapper::objectToDocument)
                .collect(Collectors.toList());
        productDAO.insert(collect);
    }

    public List<Product> getAll() {
        List<Document> all = productDAO.getAll();
        return all.stream()
                .map(mapper::documentToProduct)
                .collect(Collectors.toList());
    }
}
