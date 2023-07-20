package ua.com.poseal.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.poseal.data.Data;
import ua.com.poseal.domain.Product;
import ua.com.poseal.domain.Store;
import ua.com.poseal.dto.LeftoverDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static ua.com.poseal.util.Generator.*;

class GeneratorTest {

    Data data;
    Properties properties;
    Generator generator;

    @BeforeEach
    void init() {
        data = new Data();
        properties = new Loader().getFileProperties();
        this.generator = new Generator();
    }

    @Test
    void generateProduct() {
        int size = data.getCategories().size();

        Product product = generator.generateProduct(size);
        Long categoryId = product.getCategory().getId();
        int lengthName = product.getName().length();
        BigDecimal price = product.getPrice();

        assertNotNull(product);
        assertTrue(categoryId > 0 && categoryId <= size);
        assertTrue(lengthName >= MIN_LENGTH && lengthName <= MAX_LENGTH);
        assertTrue(price.compareTo(new BigDecimal(MIN_PRICE)) >= 0
                && price.compareTo(new BigDecimal(MAX_PRICE)) <= 0);
    }

    @Test
    void generateProducts() {
        int products = 5;
        int categories = data.getCategories().size();
        List<Product> productList = generator.generateProducts(products, categories);

        assertNotNull(productList);
        assertEquals(products, productList.size());
    }

    @Test
    void generateLeftoverDTO() {
        int limitProducts = new Random().nextInt(15);
        properties.setProperty(LEFTOVER, String.valueOf(limitProducts));
        List<Store> storeList = new ArrayList<>(data.getStores().values());
        int products = 10;
        int categories = data.getCategories().size();
        List<Product> productList = generator.generateProducts(products, categories);

        LeftoverDTO leftoverDTOS = generator.generateLeftoverDTO(storeList, productList);

        assertNotNull(leftoverDTOS);
    }
}