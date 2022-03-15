package serever;

import db.StockExchangeDao;
import model.Company;
import rx.Observable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class StockExchangeController {

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

    private <T> Observable<String> observableToStr(Observable<T> observable) {
        return observable.map(Objects::toString).onErrorReturn(Throwable::getMessage);
    }

    private String getCompanyName(Map<String, List<String>> params) {
        return params.get(Company.COMPANY_KEY).get(0);
    }

    private int getIntParam(Map<String, List<String>> params, String name) {
        return Integer.parseInt(params.get(name).get(0));
    }

    private Observable<String> withCheckedParams(
        Function<Map<String, List<String>>, Observable<String>> action,
        Map<String, List<String>> params,
        String ...paramNames
    ) {
        StringBuilder missingParams = new StringBuilder();
        for (String name : paramNames) {
            if (!params.containsKey(name)) {
                missingParams.append(name).append(';');
            }
        }
        if (missingParams.length() > 0) {
            return Observable.just(String.format("Params %s not found", missingParams.toString()));
        }

        return action.apply(params);
    }

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
