package ua.com.poseal.dao;

import java.util.List;

public interface DAO<T> {
    void insert(List<T> items);
}
