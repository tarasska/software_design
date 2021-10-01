package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.database.DBManager;
import ru.akirakozov.sd.refactoring.entities.Product;

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
            int res = statement.executeUpdate(sql);
            statement.close();
            return res;
        });
    }
}
