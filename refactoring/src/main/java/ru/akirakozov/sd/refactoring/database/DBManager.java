package ru.akirakozov.sd.refactoring.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBManager {
    Connection getConnection() throws SQLException;
}
