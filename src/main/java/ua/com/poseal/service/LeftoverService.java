package ua.com.poseal.service;

import org.bson.Document;
import ua.com.poseal.dao.LeftoverDAO;
import ua.com.poseal.domain.Product;
import ua.com.poseal.domain.Store;
import ua.com.poseal.dto.LeftoverDTO;
import ua.com.poseal.util.Generator;
import ua.com.poseal.util.Mapper;

import java.util.List;
import java.util.Properties;

public class LeftoverService {
    private final LeftoverDAO leftoverDAO;
    private final StoreService storeService;
    private final ProductService productService;
    private final Generator generator;
    private final Mapper mapper;

    public LeftoverService(Properties properties) {
        this.leftoverDAO = new LeftoverDAO(properties);
        this.storeService = new StoreService(properties);
        this.productService = new ProductService(properties);
        this.generator = new Generator(properties);
        this.mapper = new Mapper();
    }

    public void saveLeftover() {
        // find stores
        List<Store> storeList = storeService.getAll();
        // find products
        List<Product> productList = productService.getAll();
        List<Document> documents = mapper.toDocuments(generator.generateLeftoverDTO(storeList, productList));
        leftoverDAO.insertLeftover(documents);
    }

    public LeftoverDTO findAddressByCategory(String category) {
        return leftoverDAO.findAddressByCategory(category);
    }
}
