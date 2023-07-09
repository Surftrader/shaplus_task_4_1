package ua.com.poseal.data;

import ua.com.poseal.domain.Address;
import ua.com.poseal.domain.Category;
import ua.com.poseal.domain.City;
import ua.com.poseal.domain.Store;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private final Map<Integer, City> cities;
    private final Map<Integer, Category> categories;
    private final Map<Integer, Address> addresses;
    private final Map<Integer, Store> stores;

    public Data() {
        this.cities = initCities();
        this.categories = initCategory();
        this.addresses = initAddresses();
        this.stores = initStores();
    }

    private Map<Integer, Address> initAddresses() {
        Map<Integer, Address> map = new HashMap<>();
        map.put(1, new Address(1L, "пр-т Степана Бандери, 36-А", 1L));
        map.put(2, new Address(2L, "пр-т Степана Бандери, 11-А", 1L));
        map.put(3, new Address(3L, "Дніпровська наб., 13-В", 1L));
        map.put(4, new Address(4L, "вул. Стартова, 9-А", 2L));
        map.put(5, new Address(5L, "Запорізьке шосе, 62-К", 2L));
        map.put(6, new Address(6L, "вул. Набережна Заводська, 97-А", 2L));
        map.put(7, new Address(7L, "вул. Батозька, 1-В", 3L));
        map.put(8, new Address(8L, "вул. 600-річчя, 17", 3L));
        map.put(9, new Address(9L, "вул. Богдана Хмельницького, 188-А", 4L));
        map.put(10, new Address(10L, "вул. Городоцька, 302", 4L));
        map.put(11, new Address(11L, "вул. Щирецька, 7", 4L));
        map.put(12, new Address(12L, "пр-т Небесної Сотні, 2", 5L));
        map.put(13, new Address(13L, "Київське шосе, 41", 6L));
        map.put(14, new Address(14L, "вул. Героїв Праці, 9-А", 7L));
        map.put(15, new Address(15L, "пр-т Юрія Гагаріна, 352", 7L));
        map.put(16, new Address(16L, "вул. Євгена Котляра, 8/10-А", 7L));
        return map;
    }

    private Map<Integer, City> initCities() {
        Map<Integer, City> map = new HashMap<>();
        map.put(1, new City(1L, "Київ"));
        map.put(2, new City(2L, "Дніпро"));
        map.put(3, new City(3L, "Винниця"));
        map.put(4, new City(4L, "Львів"));
        map.put(5, new City(5L, "Одеса"));
        map.put(6, new City(6L, "Полтава"));
        map.put(7, new City(7L, "Харків"));
        return map;
    }

    private Map<Integer, Store> initStores() {
        Map<Integer, Store> map = new HashMap<>();
        map.put(1, new Store(1L, "ТЦ ЕПІЦЕНТР-1", 1L));
        map.put(2, new Store(2L, "ТЦ ЕПІЦЕНТР-2", 2L));
        map.put(3, new Store(3L, "ТЦ ЕПІЦЕНТР-3", 3L));
        map.put(4, new Store(4L, "ТЦ ЕПІЦЕНТР-4", 4L));
        map.put(5, new Store(5L, "ТЦ ЕПІЦЕНТР-5", 5L));
        map.put(6, new Store(6L, "ТЦ ЕПІЦЕНТР-6", 6L));
        map.put(7, new Store(7L, "ТЦ ЕПІЦЕНТР-7", 7L));
        map.put(8, new Store(8L, "ТЦ ЕПІЦЕНТР-8", 8L));
        map.put(9, new Store(9L, "ТЦ ЕПІЦЕНТР-9", 9L));
        map.put(10, new Store(10L, "ТЦ ЕПІЦЕНТР-10", 10L));
        map.put(11, new Store(11L, "ТЦ ЕПІЦЕНТР-11", 11L));
        map.put(12, new Store(12L, "ТЦ ЕПІЦЕНТР-12", 12L));
        map.put(13, new Store(13L, "ТЦ ЕПІЦЕНТР-13", 13L));
        map.put(14, new Store(14L, "ТЦ ЕПІЦЕНТР-14", 14L));
        map.put(15, new Store(15L, "ТЦ ЕПІЦЕНТР-15", 15L));
        map.put(16, new Store(16L, "ТЦ ЕПІЦЕНТР-16", 16L));
        return map;
    }

    private Map<Integer, Category> initCategory() {
        Map<Integer, Category> map = new HashMap<>();
        map.put(1, new Category(1L, "Продукти"));
        map.put(2, new Category(2L, "Зоотовари"));
        map.put(3, new Category(3L, "Електроніка"));
        map.put(4, new Category(4L, "Спортивні товари"));
        map.put(5, new Category(5L, "Канцтовари"));
        map.put(6, new Category(6L, "Побутова техніка"));
        map.put(7, new Category(7L, "Інструменти"));
        map.put(8, new Category(8L, "Краса та здоров`я"));
        map.put(9, new Category(9L, "Одяг, Взуття, Аксесуари"));
        map.put(10, new Category(10L, "Аптека"));
        return map;
    }

    public Map<Integer, City> getCities() {
        return cities;
    }

    public Map<Integer, Category> getCategories() {
        return categories;
    }

    public Map<Integer, Address> getAddresses() {
        return addresses;
    }

    public Map<Integer, Store> getStores() {
        return stores;
    }
}
