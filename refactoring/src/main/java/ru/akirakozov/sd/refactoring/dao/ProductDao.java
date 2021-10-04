package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.database.DBManager;
import ru.akirakozov.sd.refactoring.entities.Product;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDao {
    private final DBManager dbManager;

    public ProductDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void addProduct(Product product) throws SQLException {
        dbManager.executeQuery(statement -> {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";
            statement.executeUpdate(sql);
        });
    }

    public void getProducts(PrintWriter writer) throws SQLException {
        dbManager.executeQuery(statement -> {
            try (ResultSet rs = statement.executeQuery("SELECT * FROM PRODUCT")) {
                writer.println("<html><body>");

                while (rs.next()) {
                    String  name = rs.getString("name");
                    int price  = rs.getInt("price");
                    writer.println(name + "\t" + price + "</br>");
                }
                writer.println("</body></html>");
            }
        });
    }
}
