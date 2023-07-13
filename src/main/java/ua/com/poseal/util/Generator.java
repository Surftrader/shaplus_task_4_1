package ua.com.poseal.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.commons.lang3.time.StopWatch;
import ua.com.poseal.data.Data;
import ua.com.poseal.domain.Category;
import ua.com.poseal.domain.Product;
import ua.com.poseal.domain.Store;
import ua.com.poseal.dto.LeftoverDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static ua.com.poseal.App.logger;

public class Generator {
    private static final String LEFTOVER = "leftover";
    private static final double MIN_PRICE = 10;
    private static final double MAX_PRICE = 10000;
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 10;
    private static final int LETTERS_IN_ALPHABET = 26;
    private static final int MIN_AMOUNT = 1;
    private static final int MAX_AMOUNT = 10;

    private final Properties properties;
    private final Random random;
    private final Validator validator;
    private final Data data;

    private long count = 1;

    public Generator(Properties properties) {
        this.properties = properties;
        this.random = new Random();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.data = new Data();
    }

    public Product generateProduct(int categories) {
        return new Product(
                generateName(),
                generatePrice(),
                generateCategory(categories));
    }

    private Category generateCategory(int categories) {
        int nextInt = random.nextInt(categories);
        Map<Integer, Category> map = data.getCategories();
        return map.get(nextInt + 1);
    }

    private BigDecimal generatePrice() {
        double randomValue = MIN_PRICE + (MAX_PRICE - MIN_PRICE) * random.nextDouble();
        return BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP);
    }

    private String generateName() {
        int length = random.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) (random.nextInt(LETTERS_IN_ALPHABET) + 'a');
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public List<Product> generateProducts(long numbers, int categories) {
        logger.debug("Entered generateProducts() method with parameter numbers={}, categories={}", numbers, categories);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Product> products = new LinkedList<>();
        long validProductCount = 0;
        long invalidProductCount = 0;
        while (validProductCount < numbers) {
            Product product = generateProduct(categories);
            Set<ConstraintViolation<Product>> validate = validator.validate(product);
            if (validate.isEmpty()) {
                product.setId(count++);
                products.add(product);
                validProductCount++;
            } else {
                invalidProductCount++;
            }
        }

        stopWatch.stop();
        long allProducts = validProductCount + invalidProductCount;
        logger.info("{} products were generated per {} s", allProducts, stopWatch.getTime() / 1000.0);
        logger.info("{} valid products. {} invalid products.", validProductCount, invalidProductCount);
        logger.debug("Exited generateProducts() method");

        return products;
    }

    public List<LeftoverDTO> generateLeftoverDTO(List<Store> storeList, List<Product> productList) {
        logger.debug("Entered generateLeftoverDTO() method with parameter storeList={}, productList={}",
                storeList.size(), productList.size());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<LeftoverDTO> leftoverList = new LinkedList<>();
        long counter = 0;
        int limit = Integer.parseInt(properties.getProperty(LEFTOVER));
        while (counter < limit) {
            int randomIndexStore = random.nextInt(storeList.size());
            int randomIndexProduct = random.nextInt(productList.size());

            Store store = storeList.get(randomIndexStore);
            Product product = productList.get(randomIndexProduct);

            leftoverList.add(
                    new LeftoverDTO(
                            ++counter,
                            store.getName(),
                            store.getAddress().toString(),
                            product.getCategory().toString(),
                            product.getName(),
                            generateAmount()
                    )
            );
        }

        stopWatch.stop();
        logger.info("{} leftovers were generated per {} s", counter, stopWatch.getTime() / 1000.0);
        logger.debug("Exited generateLeftoverDTO() method");
        return leftoverList;
    }

    private int generateAmount() {
        return random.nextInt(MAX_AMOUNT - MIN_AMOUNT) + MIN_AMOUNT;
    }
}
