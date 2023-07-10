package ua.com.poseal.util;

import org.bson.Document;
import ua.com.poseal.domain.*;

public class Mapper {

    public Document objectToDocument(Object obj) {
        Document document = new Document();
        if (obj instanceof City) {
            City city = (City) obj;
            document.append("id", city.getId())
                    .append("name", city.getName());
        } else if (obj instanceof Address) {
            Address address = (Address) obj;
            document.append("id", address.getId())
                    .append("name", address.getName())
                    .append("cityId", address.getCityId());
        } else if (obj instanceof Store) {
            Store store = (Store) obj;
            document.append("id", store.getId())
                    .append("name", store.getName())
                    .append("addressId", store.getAddressId());
        } else if (obj instanceof Category) {
            Category category = (Category) obj;
            document.append("id", category.getId())
                    .append("name", category.getName());
        } else if (obj instanceof Product) {
            Product product = (Product) obj;
            document.append("id", product.getId())
                    .append("name", product.getName())
                    .append("price", product.getPrice())
                    .append("categoryId", product.getCategoryId());
        }
        return document;
    }
}
