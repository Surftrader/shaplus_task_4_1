package ua.com.poseal.domain;

public class Leftover {

    private Long id;
    private Store store;
    private Product product;
    private int amount;

    public Leftover(Long id, Store store, Product product, int amount) {
        this.id = id;
        this.store = store;
        this.product = product;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leftover leftover = (Leftover) o;

        return id.equals(leftover.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Leftover{" +
                "id=" + id +
                ", store=" + store +
                ", product=" + product +
                ", amount=" + amount +
                '}';
    }
}
