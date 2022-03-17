import com.mongodb.rx.client.Success;
import db.Account;
import db.AccountImpl;
import model.CompanyStockInfo;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import rx.Observable;
import server.stock.Stock;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


public class AccountTest {
    @ClassRule
    public static GenericContainer<?> stockContainer
        = new FixedHostPortGenericContainer<>("stock-exchange:1.0-SNAPSHOT")
        .withAccessToHost(true)
        .withFixedExposedPort(8080, 8080)
        .withExposedPorts(8080);

    private static final String testAddress = "http://localhost:8080/";
    private static final CompanyStockInfo huawei = new CompanyStockInfo("huawei", 1000, 10);
    private static final CompanyStockInfo dell = new CompanyStockInfo("dell", 100, 3);

    private Account account;

    @Before
    public void addTestData() throws Exception {
        addCompany(huawei);
        addCompany(dell);
        account = new AccountImpl(new Stock(testAddress));
    }

    @Test
    public void buyOkTest() {
        executeObservablesAsSequence(
            account.addUser(0),
            account.addCoins(0, 100)
        );
        assertThat(
            account.buyStocks(0, huawei.getName(), 10).toBlocking().single()
        ).isEqualTo(Success.SUCCESS);
    }

    @Test
    public void buyFailTest() {
        executeObservablesAsSequence(
            account.addUser(0),
            account.addCoins(0, 100)
        );
        assertThat(account
            .buyStocks(0, huawei.getName(), 9999)
            .map(Objects::toString)
            .onErrorReturn(Throwable::getMessage)
            .toBlocking()
            .single()
        ).isEqualTo("Only 990 shares are available on the exchange, but was requestd 9999");
    }

    @Test
    public void sellOk() {
        executeObservablesAsSequence(
            account.addUser(0),
            account.addCoins(0, 100),
            account.buyStocks(0, dell.getName(), 5)
        );
        assertThat(account
            .sellStocks(0, dell.getName(), 3)
            .toBlocking()
            .single()
        ).isEqualTo(Success.SUCCESS);
    }

    @Test
    public void allCoinsTest() {
        executeObservablesAsSequence(
            account.addUser(0),
            account.addCoins(0, 100),
            account.buyStocks(0, dell.getName(), 5)
        );
        assertThat(account
            .stockAsCoins(0)
            .toBlocking()
            .single()
        ).isEqualTo(5 * dell.getStockPrice());
    }

    private void executeObservablesAsSequence(Observable<?> ...observables) {
        for (Observable<?> observable : observables) {
            observable.toBlocking().single();
        }
    }

    private void addCompany(CompanyStockInfo company) throws Exception {
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(new URI(testAddress.concat(String.format(
                    "add_company?company=%s&stockCount=%d&stockPrice=%d",
                    company.getName(),
                    company.getStockCount(),
                    company.getStockPrice()
                ))))
                .GET()
                .build();
        HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}
