package ua.com.poseal.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product validProduct;

    Validator validator;

    @BeforeEach
    void init() {
        this.validProduct = new Product(
                1L,
                "штанга",
                new BigDecimal("2000.0"),
                new Category(1L, "Спортивні товари")

        );
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testValidProduct() {
        Set<ConstraintViolation<Product>> errors = validator.validate(validProduct);
        assertTrue(errors.isEmpty());
        assertNull(getErrorMessage());
    }

    @Test
    void testInvalidProduct() {
        // valid name length from 4 to 9
        String invalidName = "Шт";
        validProduct.setName(invalidName);
        String actualError = getErrorMessage();
        String expectedError = "Product name must contain 4 to 9 letters";

        assertEquals(expectedError, actualError);
    }

    private String getErrorMessage() {
        Set<ConstraintViolation<Product>> validate = validator.validate(validProduct);
        Iterator<ConstraintViolation<Product>> iterator = validate.iterator();
        String actualMessage = null;
        while (iterator.hasNext()) {
            ConstraintViolation<Product> next = iterator.next();
            actualMessage = next.getMessage();
        }
        return actualMessage;
    }
}