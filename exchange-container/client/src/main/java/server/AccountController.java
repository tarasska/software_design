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

    private Observable<String> stocksByUser(Map<String, List<String>> params) {
        return observableToStr(
            dao.stocksByUser(getIntParam(params, User.USER_ID_KEY))
        ).reduce("", (c1, c2) -> c1 + ", " + c2);
    }

    @Override
    public Observable<String> handle(String endpoint, Map<String, List<String>> params) {
        switch (endpoint) {
            case "add_user": return withCheckedParams(
                this::addUser,
                params,
                User.USER_ID_KEY
            );

            case "stocks_by_user": return withCheckedParams(
                this::stocksByUser,
                params,
                User.USER_ID_KEY
            );

            default: return Observable.just("Unexpected endpoint " + endpoint);
        }
    }
}
