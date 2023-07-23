package ua.com.poseal.data;

import ua.com.poseal.domain.Category;
import ua.com.poseal.domain.Store;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private Map<Integer, Category> categories;
    private Map<Integer, Store> stores;

    public Data() {
        this.categories = new HashMap<>();
        this.stores = new HashMap<>();
    }

    public Map<Integer, Category> getCategories() {
        return categories;
    }

    public Map<Integer, Store> getStores() {
        return stores;
    }

    public void setCategories(Map<Integer, Category> categories) {
        this.categories = categories;
    }

    public void setStores(Map<Integer, Store> stores) {
        this.stores = stores;
    }
}
