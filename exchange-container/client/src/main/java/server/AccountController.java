package server;

import db.AccountImpl;
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
        return Observable.just("TODO");
    }

    @Override
    public Observable<String> handle(String endpoint, Map<String, List<String>> params) {
        switch (endpoint) {
            case "add_user": return withCheckedParams(
                this::addUser,
                params,
                "TODO"
            );

            default: return Observable.just("Unexpected endpoint " + endpoint);
        }
    }
}
