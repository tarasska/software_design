package ru.akirakozov.sd.refactoring;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RealDBTest {

    protected static String TEST_DB_URL = "jdbc:sqlite:src/test/resources/test.db";

    private static void executeSql(String query) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:src/test/resources/test.db")) {
            Statement stmt = c.createStatement();

            stmt.executeUpdate(query);
            stmt.close();
        }
    }
    @BeforeAll
    public static void setup() throws SQLException {
        executeSql(
            "CREATE TABLE IF NOT EXISTS PRODUCT" +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " NAME           TEXT    NOT NULL, " +
            " PRICE          INT     NOT NULL)"
        );
    }

    @AfterAll
    public static void destroy() throws SQLException {
        executeSql("DROP TABLE IF EXISTS PRODUCT");
    }

    @BeforeEach
    public void clearDB() throws SQLException {
        executeSql("DELETE FROM PRODUCT");
    }
}
