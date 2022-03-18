package server;

import db.AccountImpl;
import model.CompanyStockInfo;
import model.User;
import rx.Observable;
import serever.AbstractController;

import java.util.List;
import java.util.Map;

public class AccountController extends AbstractController {
    private final AccountImpl dao;

    public AccountController(AccountImpl dao) {
        this.dao = dao;
    }

    private Observable<String> addUser(Map<String, List<String>> params) {
        return observableToStr(dao.addUser(getIntParam(params, User.USER_ID_KEY)));
    }

    private Observable<String> addCoins(Map<String, List<String>> params) {
        return observableToStr(dao.addCoins(
            getIntParam(params, User.USER_ID_KEY),
            getIntParam(params, User.USER_COINS_KEY)
        ));
    }

    private Observable<String> stocksAsCoins(Map<String, List<String>> params) {
        return observableToStr(
            dao.stockAsCoins(getIntParam(params, User.USER_ID_KEY))
        );
    }

    private Observable<String> stocksByUser(Map<String, List<String>> params) {
        return observableToStr(
            dao.stocksByUser(getIntParam(params, User.USER_ID_KEY))
        ).concatWith(Observable.just(";\n"));
    }

    private Observable<String> buyStocks(Map<String, List<String>> params) {
        return observableToStr(dao.buyStocks(
            getIntParam(params, User.USER_ID_KEY),
            getCompanyName(params),
            getIntParam(params, CompanyStockInfo.STOCK_CNT_KEY)
        ));
    }


    private Observable<String> sellStocks(Map<String, List<String>> params) {
        return observableToStr(dao.sellStocks(
            getIntParam(params, User.USER_ID_KEY),
            getCompanyName(params),
            getIntParam(params, CompanyStockInfo.STOCK_CNT_KEY)
        ));
    }

    @Override
    public Observable<String> handle(String endpoint, Map<String, List<String>> params) {
        switch (endpoint) {
            case "add_user": return withCheckedParams(
                this::addUser,
                params,
                User.USER_ID_KEY
            );

            case "add_coins": return withCheckedParams(
                this::addCoins,
                params,
                User.USER_ID_KEY, User.USER_COINS_KEY
            );

            case "stocks_as_coins": return withCheckedParams(
                this::stocksAsCoins,
                params,
                User.USER_ID_KEY
            );

            case "stocks_by_user": return withCheckedParams(
                this::stocksByUser,
                params,
                User.USER_ID_KEY
            );

            case "buy_stocks": return withCheckedParams(
                this::buyStocks,
                params,
                User.USER_ID_KEY, CompanyStockInfo.COMPANY_KEY, CompanyStockInfo.STOCK_CNT_KEY
            );

            case "sell_stocks": return withCheckedParams(
                this::sellStocks,
                params,
                User.USER_ID_KEY, CompanyStockInfo.COMPANY_KEY, CompanyStockInfo.STOCK_CNT_KEY
            );

            default: return Observable.just("Unexpected endpoint " + endpoint);
        }
    }
}
