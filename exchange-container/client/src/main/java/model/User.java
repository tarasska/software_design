package model;

import org.bson.Document;

import java.util.List;
import java.util.Map;

public class User implements DBEntity {
    public static final String USER_ID_KEY = "userId";
    public static final String USER_COINS_KEY = "userCoins";
    public static final String USER_STOCKS_KEY = "userStocks";

    private final int userId;
    private int coins;
    private final List<CompanyStockInfo> stocks;

    public User(int userId, int coins, List<CompanyStockInfo> stocks) {
        this.userId = userId;
        this.coins = coins;
        this.stocks = stocks;
    }

    public static User fromDocument(Document document) {
        return new User(
            document.getInteger(USER_ID_KEY),
            document.getInteger(USER_COINS_KEY),
            document.get(USER_STOCKS_KEY, List.class)
        );
    }

    @Override
    public Document toDocument() {
        return new Document(Map.of(
            USER_ID_KEY, userId,
            USER_STOCKS_KEY, stocks
        ));
    }

    public int getUserId() {
        return userId;
    }

    public int getCoins() {
        return coins;
    }

    public List<CompanyStockInfo> getStocks() {
        return stocks;
    }
}
