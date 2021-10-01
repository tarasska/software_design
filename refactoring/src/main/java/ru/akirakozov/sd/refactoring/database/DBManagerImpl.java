package ru.akirakozov.sd.refactoring.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManagerImpl implements DBManager {
    private final String connectionUrl;

    public DBManagerImpl(String url) {
        this.connectionUrl = url;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl);
    }
}
