package ua.com.poseal.dto;

public class LeftoverDTO {

    private Long id;
    private String store;
    private String address; // + city
    private String category;
    private String product;
    private int amount;

    public LeftoverDTO(Long id, String store, String address, String category, String product, int amount) {
        this.id = id;
        this.store = store;
        this.address = address;
        this.category = category;
        this.product = product;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
