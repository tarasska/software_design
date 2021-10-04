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

    private void buildDefaultHtml(PrintWriter writer, DBManager.CheckedConsumer<PrintWriter> bodyBuilder) throws SQLException {
        writer.println("<html><body>");
        bodyBuilder.accept(writer);
        writer.println("</body></html>");
    }

    private void buildFilteredProductHtml(PrintWriter writer, ResultSet rs, String header) throws SQLException {
        buildDefaultHtml(writer, w -> {
            if (header != null) {
                w.println(header);
            }
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                w.println(name + "\t" + price + "</br>");
            }
        });
    }

    private void buildInfoHtml(PrintWriter writer, ResultSet rs, String header) throws SQLException {
        buildDefaultHtml(writer, w -> {
            if (header != null) {
                w.println(header);
            }
            if (rs.next()) {
                w.println(rs.getInt(1));
            }
        });
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
                buildFilteredProductHtml(writer, rs, null);
            }
        });
    }

    public void findProductsMaxPrice(PrintWriter writer) throws SQLException {
        dbManager.executeQuery(statement -> {
            try (ResultSet rs = statement.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1")) {
                buildFilteredProductHtml(writer, rs, "<h1>Product with max price: </h1>");
            }
        });
    }

    public void findProductsMinPrice(PrintWriter writer) throws SQLException {
        dbManager.executeQuery(statement -> {
            try (ResultSet rs = statement.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1")) {
                buildFilteredProductHtml(writer, rs, "<h1>Product with min price: </h1>");
            }
        });
    }

    public void sumProductsPrice(PrintWriter writer) throws SQLException {
        dbManager.executeQuery(statement -> {
            try (ResultSet rs = statement.executeQuery("SELECT SUM(price) FROM PRODUCT")) {
                buildInfoHtml(writer, rs, "Summary price: ");
            }
        });
    }

    public void countProducts(PrintWriter writer) throws SQLException {
        dbManager.executeQuery(statement -> {
            try (ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM PRODUCT")) {
                buildInfoHtml(writer, rs, "Number of products: ");
            }
        });
    }
}
