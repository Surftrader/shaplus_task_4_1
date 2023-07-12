package ua.com.poseal.util;

import org.bson.Document;
import org.bson.types.Decimal128;
import ua.com.poseal.domain.*;
import ua.com.poseal.dto.LeftoverDTO;

public class Mapper {

    public Document objectToDocument(Object obj) {
        Document document = new Document();
        if (obj instanceof City) {
            City city = (City) obj;
            document.append("_id", city.getId())
                    .append("name", city.getName());
        } else if (obj instanceof Address) {
            Address address = (Address) obj;
            document.append("_id", address.getId())
                    .append("name", address.getName())
                    .append("city", objectToDocument(address.getCity()));
        } else if (obj instanceof Store) {
            Store store = (Store) obj;
            document.append("_id", store.getId())
                    .append("name", store.getName())
                    .append("address", objectToDocument(store.getAddress()));
        } else if (obj instanceof Category) {
            Category category = (Category) obj;
            document.append("_id", category.getId())
                    .append("name", category.getName());
        } else if (obj instanceof Product) {
            Product product = (Product) obj;
            document.append("_id", product.getId())
                    .append("name", product.getName())
                    .append("price", product.getPrice())
                    .append("category", objectToDocument(product.getCategory()));
        } else if (obj instanceof Leftover) {
            Leftover leftover = (Leftover) obj;
            document.append("_id", leftover.getId())
                    .append("store", objectToDocument(leftover.getStore()))
                    .append("product", objectToDocument(leftover.getProduct()))
                    .append("amount", leftover.getAmount());
        } else if (obj instanceof LeftoverDTO) {
            LeftoverDTO leftover = (LeftoverDTO) obj;
            document.append("_id", leftover.getId())
                    .append("store", leftover.getStore())
                    .append("address", leftover.getAddress())
                    .append("category", leftover.getCategory())
                    .append("product", leftover.getProduct())
                    .append("amount", leftover.getAmount());
        }
        return document;
    }

    public Product documentToProduct(Document document) {
        return new Product(
                document.getLong("_id"),
                document.getString("name"),
                document.get("price", Decimal128.class).bigDecimalValue(),
                documentToCategory(document.get("category", Document.class))
        );
    }

    public Category documentToCategory(Document document) {
        return new Category(
                document.getLong("_id"),
                document.getString("name")
        );
    }

    public Store documentToStore(Document document) {
        return new Store(
                document.getLong("_id"),
                document.getString("name"),
                documentToAddress(document.get("address", Document.class))
        );
    }

    public Address documentToAddress(Document document) {
        return new Address(
                document.getLong("_id"),
                document.getString("name"),
                documentToCity(document.get("city", Document.class))
        );
    }

    public City documentToCity(Document document) {
        return new City(
                document.getLong("_id"),
                document.getString("name")
        );
    }
}
