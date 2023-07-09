package ua.com.poseal.domain;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public class Product {
    private Long id;
    @Length(min = 4, max = 9, message = "Product name must contain 4 to 9 letters")
    private String name;
    private final BigDecimal price;
    private final Long categoryId;

    public Product(String name, BigDecimal price, Long categoryId) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", categoryId=" + categoryId +
                '}';
    }
}
