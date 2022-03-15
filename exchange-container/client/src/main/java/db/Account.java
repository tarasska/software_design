package db;

import com.mongodb.rx.client.Success;
import model.CompanyStockInfo;
import rx.Observable;

public interface Account {
    Observable<Success> addUser(int userId);
    Observable<Success> addCoins(int userId, int count);

    Observable<Integer> stockAsCoins(int userId);
    Observable<CompanyStockInfo> stocksByUser(int userId);

    Observable<Success> buyStocks(int userId, String companyName, int count);
    Observable<Success> sellStocks(int userId, String companyName, int count);
}
