package ua.com.poseal.service;

import ua.com.poseal.dao.DAO;
import ua.com.poseal.dao.StoreDAO;
import ua.com.poseal.data.Data;
import ua.com.poseal.domain.Store;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class StoreService {
    private final DAO<Store> storeDAO;
    private final Data data;

    public StoreService(Properties properties) {
        this.storeDAO = new StoreDAO(properties);
        this.data = new Data();
    }

    public List<Store> getAll() {
        return storeDAO.getAll();
    }

    public void saveCollection() {
        List<Store> stores = fillCollection();
        storeDAO.insert(stores);
    }

    private List<Store> fillCollection() {
        List<Store> stores = new LinkedList<>();
        Map<Integer, Store> map = data.getStores();
        for (Map.Entry<Integer, Store> entry : map.entrySet()) {
            stores.add(entry.getValue());
        }
        return stores;
    }
}
