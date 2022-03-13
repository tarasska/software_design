package model;

import org.bson.Document;

import java.util.Map;

public class Company implements DBEntity {
    private final String name;
    private int stockCount;
    private int stockPrice;

    public Company(String name, int stockCount, int stockPrice) {
        this.name = name;
        this.stockCount = stockCount;
        this.stockPrice = stockPrice;
    }

    public static Company fromDocument(Document document) {
        return new Company(
            document.getString("company"),
            document.getInteger("stockCount"),
            document.getInteger("stockPrice")
        );
    }

    @Override
    public Document toDocument() {
        return new Document(Map.of(
           "company", name,
           "stockCount", stockCount,
           "stockPrice", stockPrice
        ));
    }
}
