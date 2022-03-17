package server.stock;

import model.CompanyStockInfo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Stock implements StockApi {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String stockAddress;

    public Stock(String stockAddress) {
        this.stockAddress = stockAddress;
    }

    private String companyRequest(String endpoint, String company) {
        return String.format(
            "%s?%s=%s",
            endpoint,
            CompanyStockInfo.COMPANY_KEY, company
        );
    }

    private String countRequest(String endpoint, String company, int count) {
        return String.format(
            "%s?%s=%s&%s=%d",
            endpoint,
            CompanyStockInfo.COMPANY_KEY, company,
            CompanyStockInfo.STOCK_CNT_KEY, count
        );
    }

    private String sendRequest(String request) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(stockAddress.concat(request)))
                .GET()
                .build();
            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body().trim();
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new IllegalStateException("Request failed unexpectedly." + e.getMessage(), e);
        }
    }

    private int toIntResponse(String response) {
        try {
            return Integer.parseInt(response);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unexpected response from stock: " + response);
        }
    }

    private void expectedSuccess(String response) {
        if (!"SUCCESS".equals(response)) {
            throw new IllegalStateException("Request failed unexpectedly: " + response);
        }
    }

    @Override
    public int stockPrice(String company) {
        return toIntResponse(sendRequest(companyRequest("get_stock_price", company)));
    }

    @Override
    public int availableStock(String company) {
        return toIntResponse(sendRequest(companyRequest("get_stock_count", company)));
    }

    @Override
    public void buyStocks(String company, int count) {
        expectedSuccess(sendRequest(countRequest("buy_stocks", company, count)));
    }

    @Override
    public void sellStocks(String company, int count) {
        expectedSuccess(sendRequest(countRequest("add_stocks", company, count)));
    }
}
