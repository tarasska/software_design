package serever;

import com.mongodb.rx.client.Success;
import db.StockExchangeDao;
import model.Company;
import rx.Observable;

import java.util.List;
import java.util.Map;

public class StockExchangeController {

    private final StockExchangeDao dao;

    public StockExchangeController(StockExchangeDao dao) {
        this.dao = dao;
    }

    private Observable<String> addCompany(Map<String, List<String>> params) {
        return Observable.just("TODO");
    }

    private Observable<String> addStocks(Map<String, List<String>> params) {
        return Observable.just("TODO");
    }

    private Observable<String> getCompanies(Map<String, List<String>> params) {
        return Observable.just("TODO");
    }

    private Observable<String> getCompany(Map<String, List<String>> params) {
        return Observable.just("TODO");
    }

    private Observable<String> updateStockPrices(Map<String, List<String>> params) {
        return Observable.just("TODO");
    }

    private Observable<String> buyStocks(Map<String, List<String>> params) {
        return Observable.just("TODO");
    }

    public Observable<String> handle(String endpoint, Map<String, List<String>> params) {
        switch (endpoint) {
            case "add_company": return addCompany(params);

            case "add_stocks": return addStocks(params);

            case "get_companies": return getCompanies(params);

            case "get_company": return getCompany(params);

            case "update_stock_prices": return updateStockPrices(params);

            case "buy_stocks": return buyStocks(params);
            
            default: return Observable.just("Unexpected endpoint " + endpoint);
        }
    }
}
