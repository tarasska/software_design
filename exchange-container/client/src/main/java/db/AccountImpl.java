package db;

import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.FindObservable;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import model.CompanyStockInfo;
import model.User;
import org.bson.Document;
import rx.Observable;
import server.stock.Stock;

import java.util.List;
import java.util.function.Function;

public class AccountImpl implements Account {
    private final MongoCollection<Document> users = MongoDB.getUsersEmptyCollection();
    private final Stock stock;

    public AccountImpl(Stock stock) {
        this.stock = stock;
    }

    private <T extends Document> Observable<User> mapUser(FindObservable<T> found) {
        return found.toObservable().map(User::fromDocument).defaultIfEmpty(null);
    }

    private Observable<User> findUser(int userId) {
        return mapUser(users.find(Filters.eq(User.USER_ID_KEY, userId))).defaultIfEmpty(null);
    }

    private Observable<Success> replaceIfNonNull(
        int userId,
        Function<User, User> mapper
    ) {
        return findUser(userId).flatMap(company -> {
            if (company == null) {
                return Observable.error(new IllegalArgumentException(String.format(
                    "Provided user %d not exists.", userId
                )));
            }

            try {
                User updatedCompany = mapper.apply(company);
                return users
                    .replaceOne(
                        Filters.eq(User.USER_ID_KEY, userId),
                        updatedCompany.toDocument()
                    )
                    .map(document -> Success.SUCCESS);
            } catch (Throwable e) {
                return Observable.error(e);
            }
        });
    }

    @Override
    public Observable<Success> addUser(int userId) {
        return findUser(userId).flatMap(user -> {
            if (user != null) {
                return Observable.error(new IllegalArgumentException(String.format(
                    "Unable to add user %s, because it already exist.",
                    userId
                )));
            } else {
                return users.insertOne(new User(userId, 0, List.of()).toDocument());
            }
        });
    }

    @Override
    public Observable<Success> addCoins(int userId, int count) {
        return replaceIfNonNull(userId, user -> {
            user.addCoins(count);
            return user;
        });
    }

    @Override
    public Observable<Integer> stockAsCoins(int userId) {
        return findUser(userId).flatMap(user -> {
            if (user == null) {
                return Observable.error(new IllegalArgumentException(String.format(
                    "Unable to find user %s",
                    userId
                )));
            }
            try {
                int sum = 0;
                for (CompanyStockInfo info : user.getStocks()) {
                    if (info.getStockCount() > 0) {
                        int price = stock.stockPrice(info.getName());
                        sum += price * info.getStockCount();
                    }
                }
                return Observable.just(sum);
            } catch (Throwable t) {
                Observable.error(t);
            }
            return null;
        });
    }

    @Override
    public Observable<CompanyStockInfo> stocksByUser(int userId) {
        return findUser(userId).flatMap(user -> {
            if (user == null) {
                return Observable.error(new IllegalArgumentException(String.format(
                    "Unable to find user %s",
                    userId
                )));
            }
            return Observable.from(user.getStocks());
        });
    }

    @Override
    public Observable<Success> buyStocks(int userId, String companyName, int count) {
        return replaceIfNonNull(userId, user -> {
            int availableStockCount = stock.availableStock(companyName);
            if (availableStockCount < count) {
                throw new IllegalArgumentException(String.format(
                    "Only %d shares are available on the exchange, but was requestd %d",
                    availableStockCount,
                    count
                ));
            }
            int stockPrice = stock.stockPrice(companyName);
            if (user.getCoins() < stockPrice * count) {
                throw new IllegalArgumentException(String.format(
                    "There is not enough money on the account, you need %d coins, but there are only %d.",
                    stockPrice * count,
                    user.getCoins()
                ));
            }
            stock.buyStocks(companyName, count);
            user.addStocks(companyName, count);
            return user;
        });
    }

    @Override
    public Observable<Success> sellStocks(int userId, String companyName, int count) {
        return replaceIfNonNull(userId, user -> {
            int userStockCount = user.stockCountByName(companyName);
            if (userStockCount < count) {
                throw new IllegalArgumentException(String.format(
                    "Not enough stocks to sell, %d requested, only %d available.",
                    count,
                    userStockCount
                ));
            }
            int stockPrice = stock.stockPrice(companyName);
            stock.sellStocks(companyName, count);
            user.addCoins(stockPrice * count);
            user.addStocks(companyName, -count);
            return user;
        });
    }
}
