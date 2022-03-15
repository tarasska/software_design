package db;

import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.FindObservable;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import model.CompanyStockInfo;
import org.bson.Document;
import rx.Observable;

import java.util.function.Function;

public class StockExchangeDao implements StockExchange {

    private final MongoCollection<Document> companies = MongoDB.getCompanies();

    private <T extends Document> Observable<CompanyStockInfo> mapCompany(FindObservable<T> found) {
        return found.toObservable().map(CompanyStockInfo::fromDocument);
    }

    private Observable<CompanyStockInfo> findCompany(String company) {
        return mapCompany(companies.find(Filters.eq(CompanyStockInfo.COMPANY_KEY, company))).defaultIfEmpty(null);
    }

    private Observable<Success> replaceIfNonNull(
        String companyName,
        Function<CompanyStockInfo, CompanyStockInfo> mapper
    ) {
        return findCompany(companyName).flatMap(company -> {
            if (company == null) {
                return Observable.error(new IllegalArgumentException(String.format(
                    "Provided company %s not exists.", companyName
                )));
            }
            CompanyStockInfo updatedCompany = mapper.apply(company);

            return companies
                .replaceOne(
                    Filters.eq(CompanyStockInfo.COMPANY_KEY, companyName),
                    updatedCompany.toDocument()
                )
                .map(document -> Success.SUCCESS);
        });
    }

    @Override
    public Observable<Success> addCompany(String name, int stocksCount, int stocksPrice) {
        return findCompany(name).flatMap(company -> {
           if (company != null) {
               return Observable.error(new IllegalArgumentException(String.format(
                   "Unable to add company %s, because it already exist.",
                   name
               )));
           } else {
               return companies.insertOne(new CompanyStockInfo(name, stocksCount, stocksPrice).toDocument());
           }
        });
    }

    @Override
    public Observable<Success> addStocks(String company, int stocksCount) {
        return replaceIfNonNull(company, (companyObj) -> {
            companyObj.addStocks(stocksCount);
            return companyObj;
        });
    }

    @Override
    public Observable<CompanyStockInfo> getCompanies() {
        return mapCompany(companies.find());
    }

    @Override
    public Observable<CompanyStockInfo> getCompany(String company) {
        return findCompany(company);
    }

    @Override
    public Observable<Success> updateStockPrices(String company, int newPrice) {
        return replaceIfNonNull(company, (companyObj) -> {
            companyObj.setStockPrice(newPrice);
            return companyObj;
        });
    }

    @Override
    public Observable<Success> buyStocks(String company, int stocksCount) {
        return replaceIfNonNull(company, (companyObj) -> {
            companyObj.addStocks(-stocksCount);
            return companyObj;
        });
    }
}
