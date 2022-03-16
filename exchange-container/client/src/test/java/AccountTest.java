import db.Account;
import db.AccountImpl;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import server.stock.Stock;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class AccountTest {
    @ClassRule
    public static GenericContainer<?> stockContainer
        = new GenericContainer<>("stock-exchange:1.0-SNAPSHOT")
        .withAccessToHost(true)
        .withExposedPorts(27017, 8080);

    private static final String testAddress = "http://localhost:8080/";
    private static final String testUri = testAddress.concat("add_company?company=test&stockCount=100&stockPrice=2");

    private Account account;

    @Before
    public void addTestData() throws Exception {
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(new URI(testUri))
                .GET()
                .build();
        HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        account = new AccountImpl(new Stock(testAddress));
        account.addUser(0);
        account.addCoins(0, 100);
    }

    @Test
    public void buyTest() {
        Assertions.assertThat(
            account.buyStocks(0, "test", 10).toBlocking().single()
        ).isEqualTo("SUCCESS");
    }
}
