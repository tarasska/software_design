package model;

import org.bson.Document;

import java.util.Map;

public class CompanyStockInfo implements DBEntity {
    public static final String COMPANY_KEY = "company";
    public static final String STOCK_CNT_KEY = "stockCount";
    public static final String STOCK_PRICE_KEY = "stockPrice";

    private final String name;
    private int stockCount;
    private int stockPrice;

    public CompanyStockInfo(String name, int stockCount, int stockPrice) {
        this.name = name;
        this.stockCount = stockCount;
        this.stockPrice = stockPrice;
    }

    public static CompanyStockInfo fromDocument(Document document) {
        return new CompanyStockInfo(
            document.getString(COMPANY_KEY),
            document.getInteger(STOCK_CNT_KEY),
            document.getInteger(STOCK_PRICE_KEY)
        );
    }

    @Override
    public Document toDocument() {
        return new Document(Map.of(
            COMPANY_KEY, name,
            STOCK_CNT_KEY, stockCount,
            STOCK_PRICE_KEY, stockPrice
        ));
    }

    public void addStocks(int delta) {
        this.stockCount += delta;
    }

    public void setStockPrice(int newPrice) {
        this.stockPrice = newPrice;
    }

    @Override
    public String toString() {
        return String.format("{Name: %s; Stocks: %d, Price: %d}", name, stockCount, stockPrice);
    }
}
