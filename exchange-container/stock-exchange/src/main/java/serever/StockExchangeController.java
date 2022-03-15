package serever;

import db.StockExchangeDao;
import model.Company;
import rx.Observable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StockExchangeController extends AbstractController {

    private final StockExchangeDao dao;

    public StockExchangeController(StockExchangeDao dao) {
        this.dao = dao;
    }

    private Observable<String> addCompany(Map<String, List<String>> params) {
        return observableToStr(dao.addCompany(
            getCompanyName(params),
            getIntParam(params, Company.STOCK_CNT_KEY),
            getIntParam(params, Company.STOCK_PRICE_KEY)
        ));
    }

    private Observable<String> addStocks(Map<String, List<String>> params) {
        return observableToStr(dao.addStocks(
            getCompanyName(params),
            getIntParam(params, Company.STOCK_CNT_KEY)
        ));
    }

    private Observable<String> getCompanies(Map<String, List<String>> params) {
        return dao.getCompanies().map(Objects::toString).reduce("", (c1, c2) -> c1 + ", " + c2);
    }

    private Observable<String> getCompany(Map<String, List<String>> params) {
        return observableToStr(dao.getCompany(getCompanyName(params)));
    }

    private Observable<String> updateStockPrices(Map<String, List<String>> params) {
        return observableToStr(dao.updateStockPrices(
            getCompanyName(params),
            getIntParam(params, Company.STOCK_PRICE_KEY)
        ));
    }

    private Observable<String> buyStocks(Map<String, List<String>> params) {
        return observableToStr(dao.buyStocks(
            getCompanyName(params),
            getIntParam(params, Company.STOCK_CNT_KEY)
        ));
    }

    @Override
    public Observable<String> handle(String endpoint, Map<String, List<String>> params) {
        switch (endpoint) {
            case "add_company": return withCheckedParams(
                this::addCompany,
                params,
                Company.COMPANY_KEY, Company.STOCK_CNT_KEY, Company.STOCK_PRICE_KEY
            );

            case "add_stocks": return withCheckedParams(
                this::addStocks,
                params,
                Company.COMPANY_KEY, Company.STOCK_CNT_KEY
            );

            case "get_companies": return withCheckedParams(
                this::getCompanies,
                params
            );

            case "get_company": return withCheckedParams(
                this::getCompany,
                params,
                Company.COMPANY_KEY
            );

            case "update_stock_prices": return withCheckedParams(
                this::updateStockPrices,
                params,
                Company.COMPANY_KEY, Company.STOCK_PRICE_KEY
            );

            case "buy_stocks": return withCheckedParams(
                this::buyStocks,
                params,
                Company.COMPANY_KEY, Company.STOCK_CNT_KEY
            );

            default: return Observable.just("Unexpected endpoint " + endpoint);
        }
    }
}
