package ru.akirakozov.sd.refactoring.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface DBManager {
    @FunctionalInterface
    interface CheckedFun<T, R> {
        R apply(T t) throws SQLException;
    }

    Connection getConnection() throws SQLException;

    default <R> R executeQuery(CheckedFun<Statement, R> query) throws SQLException {
        try (Connection c = getConnection()) {
            return query.apply(c.createStatement());
        }
    }
}
