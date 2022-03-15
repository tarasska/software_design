package db;

import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import model.CompanyStockInfo;
import org.bson.Document;
import rx.Observable;

public class AccountImpl implements Account {
    private final MongoCollection<Document> users = MongoDB.getUsers();

    @Override
    public Observable<Success> addUser(int userId) {
        return null;
    }

    @Override
    public Observable<Success> addCoins(int userId, int count) {
        return null;
    }

    @Override
    public Observable<Integer> stockAsCoins(int userId) {
        return null;
    }

    @Override
    public Observable<CompanyStockInfo> stocksByUser(int userId) {
        return null;
    }

    @Override
    public Observable<Success> buyStocks(int userId, String companyName, int count) {
        return null;
    }

    @Override
    public Observable<Success> sellStocks(int userId, String companyName, int count) {
        return null;
    }
}
