package db;

import com.mongodb.rx.client.Success;
import model.CompanyStockInfo;
import rx.Observable;

public interface StockExchange {
    Observable<Success> addCompany(String name, int stocksCount, int stocksPrice);
    Observable<Success> addStocks(String company, int stocksCount);

    Observable<CompanyStockInfo> getCompanies();
    Observable<CompanyStockInfo> getCompany(String company);

    Observable<Success> updateStockPrices(String company, int newPrice);
    Observable<Success> buyStocks(String company, int stocksCount);
}
