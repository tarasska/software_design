package db;

import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import model.Company;
import org.bson.Document;
import rx.Observable;

public class StockExchangeDao implements StockExchange {

    private final MongoCollection<Document> companies = MongoDB.getCompanies();

    @Override
    public Observable<Success> addCompany(String name, int stocksCount, int stocksPrice) {
        return null;
    }

    @Override
    public Observable<Success> addStocks(String company, int stocksCount) {
        return null;
    }

    @Override
    public Observable<Company> getCompanies() {
        return null;
    }

    @Override
    public Observable<Company> getCompany(String company) {
        return null;
    }

    @Override
    public Observable<Success> updateStockPrices(String company, int newPrice) {
        return null;
    }

    @Override
    public Observable<Success> buyStocks(String company, int stocksCount) {
        return null;
    }
}
