package ua.com.poseal.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.commons.lang3.time.StopWatch;
import ua.com.poseal.domain.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static ua.com.poseal.App.logger;

public class ProductGenerator {

    private static final double MIN_PRICE = 10;
    private static final double MAX_PRICE = 10000;
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 10;
    private static final int LETTERS_IN_ALPHABET = 26;
    private final Random random;
    private final Validator validator;

    public ProductGenerator() {
        this.random = new Random();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public Product generateProduct(int categories) {
        return new Product(
                generateName(),
                generatePrice(),
                generateId(categories));
    }

    protected Long generateId(int categories) {
        int nextInt = random.nextInt(categories);
        return (long) nextInt + 1;
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
        logger.debug("Entered generateProducts() method with parameter numbers={}", numbers);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Product> products = new LinkedList<>();
        long validProductCount = 0;
        long invalidProductCount = 0;
        while (validProductCount < numbers) {
            Product product = generateProduct(categories);
            Set<ConstraintViolation<Product>> validate = validator.validate(product);
            if (validate.isEmpty()) {
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
}
