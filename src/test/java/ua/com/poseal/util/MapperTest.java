package ua.com.poseal.util;

import org.bson.Document;
import org.bson.types.Decimal128;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.poseal.domain.*;
import ua.com.poseal.dto.LeftoverDTO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

    Mapper mapper;
    Product product;
    Category category;
    Store store;
    Address address;
    City city;

    @BeforeEach
    void init() {
        this.mapper = new Mapper();
    }

    @Test
    void objectToDocument() {
        City city = new City(1L, "cityName");
        Address address = new Address(2L, "addressName", city);
        Store store = new Store(3L, "storeName", address);
        Category category = new Category(4L, "categoryName");
        Product product = new Product(5L, "productName", new BigDecimal("9.99"), category);
        Leftover leftover = new Leftover(6L, store, product, 10);
        LeftoverDTO leftoverDTO = new LeftoverDTO(
                7L, "storeName", "addressName",
                "categoryName", "productName", 5);

        Document cityDocument = mapper.objectToDocument(city);
        assertEquals(1L, cityDocument.getLong("_id").longValue());
        assertEquals("cityName", cityDocument.getString("name"));

        // Address
        Document addressDocument = mapper.objectToDocument(address);
        assertEquals(2L, addressDocument.getLong("_id").longValue());
        assertEquals("addressName", addressDocument.getString("name"));
        assertEquals(cityDocument, addressDocument.get("city", Document.class));

        // Store
        Document storeDocument = mapper.objectToDocument(store);
        assertEquals(3L, storeDocument.getLong("_id").longValue());
        assertEquals("storeName", storeDocument.getString("name"));
        assertEquals(addressDocument, storeDocument.get("address", Document.class));

        // Category
        Document categoryDocument = mapper.objectToDocument(category);
        assertEquals(4L, categoryDocument.getLong("_id").longValue());
        assertEquals("categoryName", categoryDocument.getString("name"));

        // Product
        Document productDocument = mapper.objectToDocument(product);
        assertEquals(5L, productDocument.getLong("_id").longValue());
        assertEquals("productName", productDocument.getString("name"));
        assertEquals(0, new BigDecimal("9.99")
                .compareTo(productDocument.get("price", BigDecimal.class)));
        assertEquals(categoryDocument, productDocument.get("category", Document.class));

        // Leftover
        Document leftoverDocument = mapper.objectToDocument(leftover);
        assertEquals(6L, leftoverDocument.getLong("_id").longValue());
        assertEquals(storeDocument, leftoverDocument.get("store", Document.class));
        assertEquals(productDocument, leftoverDocument.get("product", Document.class));
        assertEquals(10, leftoverDocument.getInteger("amount").intValue());

        // LeftoverDTO
        Document leftoverDTODocument = mapper.objectToDocument(leftoverDTO);
        assertEquals(7L, leftoverDTODocument.getLong("_id").longValue());
        assertEquals("storeName", leftoverDTODocument.getString("store"));
        assertEquals("addressName", leftoverDTODocument.getString("address"));
        assertEquals("categoryName", leftoverDTODocument.getString("category"));
        assertEquals("productName", leftoverDTODocument.getString("product"));
        assertEquals(5, leftoverDTODocument.getInteger("amount").intValue());
    }

    @Test
    void documentToProduct() {
        Document document = new Document("_id", 123L)
                .append("name", "productName")
                .append("price", Decimal128.parse("9.99"))
                .append("category",
                        new Document("_id", 456L)
                                .append("name", "categoryName"));

        product = mapper.documentToProduct(document);

        assertEquals(123L, product.getId());
        assertEquals("productName", product.getName());
        assertEquals(new BigDecimal("9.99"), product.getPrice());
        assertEquals(new Category(456L, "categoryName"), product.getCategory());
    }

    @Test
    void documentToCategory() {
        Document document = new Document("_id", 123L)
                .append("name", "categoryName");

        category = mapper.documentToCategory(document);

        assertEquals(123L, category.getId());
        assertEquals("categoryName", category.getName());
    }

    @Test
    void documentToStore() {
        Document document = new Document("_id", 123L)
                .append("name", "storeName")
                .append("address",
                        new Document("_id", 456L)
                                .append("street", "streetName")
                                .append("city",
                                        new Document("_id", 789L)
                                                .append("name", "cityName")));

        store = mapper.documentToStore(document);

        assertEquals(123L, store.getId());
        assertEquals("storeName", store.getName());
        assertEquals(new Address(456L, "streetName",
                new City(789L, "cityName")), store.getAddress());
    }

    @Test
    void documentToAddress() {
        Document document = new Document("_id", 123L)
                .append("name", "addressName")
                .append("city", new Document("_id", 456L).append("name", "cityName"));

        address = mapper.documentToAddress(document);

        assertEquals(123L, address.getId());
        assertEquals("addressName", address.getName());
        assertEquals(new City(456L, "cityName"), address.getCity());
    }

    @Test
    void documentToCity() {
        Document document = new Document("_id", 123L)
                .append("name", "cityName");

        city = mapper.documentToCity(document);

        assertEquals(123L, city.getId());
        assertEquals("cityName", city.getName());
    }

    @Test
    void toDocuments() {
        LeftoverDTO dto1 = new LeftoverDTO(
                1L, "Store A", "Address A", "Category X", "Product A", 10);
        LeftoverDTO dto2 = new LeftoverDTO(
                2L, "Store B", "Address B", "Category Y", "Product B", 20);
        LeftoverDTO dto3 = new LeftoverDTO(
                3L, "Store C", "Address C", "Category X", "Product C", 30);

        List<LeftoverDTO> dtos = Arrays.asList(dto1, dto2, dto3);

        List<Document> documents = mapper.toDocuments(dtos);

        assertEquals(dtos.size(), documents.size());

        for (int i = 0; i < dtos.size(); i++) {
            LeftoverDTO dto = dtos.get(i);
            Document document = documents.get(i);

            assertEquals(dto.getId(), document.getLong("_id"));
            assertEquals(dto.getStore(), document.getString("store"));
            assertEquals(dto.getAddress(), document.getString("address"));
            assertEquals(dto.getCategory(), document.getString("category"));
            assertEquals(dto.getProduct(), document.getString("product"));
            assertEquals(dto.getAmount(), document.getInteger("amount").intValue());
        }
    }
}
