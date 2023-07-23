package ua.com.poseal.service;

import org.bson.Document;
import ua.com.poseal.dao.DAO;
import ua.com.poseal.dao.ProductDAO;
import ua.com.poseal.data.Data;
import ua.com.poseal.domain.Product;
import ua.com.poseal.util.Generator;
import ua.com.poseal.util.Mapper;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ProductService {
    private final Data data;
    private final DAO<Document> productDAO;
    private final Generator generator;
    private final Mapper mapper;

    public ProductService(Properties properties, Data data) {
        this.data = data;
        this.productDAO = new ProductDAO(properties);
        this.generator = new Generator(data);
        this.mapper = new Mapper();
    }

    public void saveProducts(long numbers) {
        long categories = data.getCategories().size();
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
