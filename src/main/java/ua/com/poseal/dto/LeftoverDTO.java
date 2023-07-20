package ua.com.poseal.dto;

public class LeftoverDTO {
    private Long id;
    private String store;
    private final String address;
    private String category;
    private String product;
    private final int amount;

    public LeftoverDTO(Long id, String store, String address, String category, String product, int amount) {
        this.id = id;
        this.store = store;
        this.address = address;
        this.category = category;
        this.product = product;
        this.amount = amount;
    }

    public LeftoverDTO(String store, String address, String category, String product, int amount) {
        this.store = store;
        this.address = address;
        this.category = category;
        this.product = product;
        this.amount = amount;
    }

    public LeftoverDTO(String address, int totalAmount) {
        this.address = address;
        this.amount = totalAmount;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
}
