package db;

import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.FindObservable;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import model.Company;
import org.bson.Document;
import rx.Observable;

import java.util.function.Function;

public class StockExchangeDao implements StockExchange {

    private final MongoCollection<Document> companies = MongoDB.getCompanies();

    private <T extends Document> Observable<Company> mapCompany(FindObservable<T> found) {
        return found.toObservable().map(Company::fromDocument);
    }

    private Observable<Company> findCompany(String company) {
        return mapCompany(companies.find(Filters.eq(Company.COMPANY_KEY, company))).defaultIfEmpty(null);
    }

    private Observable<Success> replaceIfNonNull(
        String companyName,
        Function<Company, Company> mapper
    ) {
        return findCompany(companyName).flatMap(company -> {
            if (company == null) {
                return Observable.error(new IllegalArgumentException(String.format(
                    "Provided company %s not exists.", companyName
                )));
            }
            Company updatedCompany = mapper.apply(company);

            return companies
                .replaceOne(
                    Filters.eq(Company.COMPANY_KEY, companyName),
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
               return companies.insertOne(new Company(name, stocksCount, stocksPrice).toDocument());
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
    public Observable<Company> getCompanies() {
        return mapCompany(companies.find());
    }

    @Override
    public Observable<Company> getCompany(String company) {
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
