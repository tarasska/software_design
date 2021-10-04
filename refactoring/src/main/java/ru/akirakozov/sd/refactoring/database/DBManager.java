package ru.akirakozov.sd.refactoring.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface DBManager {
    @FunctionalInterface
    interface CheckedConsumer<T> {
        void accept(T t) throws SQLException;
    }

    Connection getConnection() throws SQLException;

    default void executeQuery(CheckedConsumer<Statement> query) throws SQLException {
        try (
            Connection c = getConnection();
            Statement statement = c.createStatement()
        ) {
            query.accept(statement);
        }
    }
}
