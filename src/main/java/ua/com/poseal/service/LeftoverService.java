package ua.com.poseal.service;

import ua.com.poseal.dao.LeftoverDAO;
import ua.com.poseal.domain.Product;
import ua.com.poseal.domain.Store;
import ua.com.poseal.dto.LeftoverDTO;
import ua.com.poseal.util.Generator;

import java.util.List;
import java.util.Properties;

public class LeftoverService {
    private final LeftoverDAO leftoverDAO;
    private final StoreService storeService;
    private final ProductService productService;
    private final Generator generator;

    public LeftoverService(Properties properties) {
        this.leftoverDAO = new LeftoverDAO(properties);
        this.storeService = new StoreService(properties);
        this.productService = new ProductService(properties);
        this.generator = new Generator(properties);
    }

    public void saveLeftover() {
        // find stores
        List<Store> storeList = storeService.getAll();
        // find products
        List<Product> productList = productService.getAll();
        leftoverDAO.insertLeftoverDTO(generator.generateLeftoverDTO(storeList, productList));
    }

    public LeftoverDTO findAddressByCategory(String category) {
        return leftoverDAO.findAddressByCategory(category);
    }
}
