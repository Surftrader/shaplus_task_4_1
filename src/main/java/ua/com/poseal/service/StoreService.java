package ua.com.poseal.service;

import org.bson.Document;
import ua.com.poseal.dao.DAO;
import ua.com.poseal.dao.StoreDAO;
import ua.com.poseal.data.Data;
import ua.com.poseal.domain.Store;
import ua.com.poseal.util.Mapper;

import java.util.*;
import java.util.stream.Collectors;

public class StoreService {
    private final DAO<Document> storeDAO;
    private final Data data;
    private final Mapper mapper;

    public StoreService(Properties properties, Data data) {
        this.data = data;
        this.storeDAO = new StoreDAO(properties);
        this.mapper = new Mapper();
    }

    public List<Store> getAll() {
        List<Document> all = storeDAO.getAll();
        return all.stream()
                .map(mapper::documentToStore)
                .collect(Collectors.toList());

    }

    public void saveCollection() {
        List<Store> stores = fillCollection();
        List<Document> collect = stores.stream()
                .map(mapper::objectToDocument)
                .collect(Collectors.toList());
        storeDAO.insert(collect);
    }

    private List<Store> fillCollection() {
        Map<Integer, Store> map = data.getStores();
        return new ArrayList<>(map.values());
    }
}
