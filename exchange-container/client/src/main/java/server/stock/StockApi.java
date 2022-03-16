package server.stock;

public interface StockApi {
    int stockPrice(String company);
    int availableStock(String company);

    void buyStocks(String company, int count);
    void sellStocks(String company, int count);
}
