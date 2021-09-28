package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class BaseServletRealDBTest {
    @Mock
    protected HttpServletRequest request;

    @Mock
    protected HttpServletResponse response;

    @BeforeAll
    public static void setup() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:src/test/resources/test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    @AfterAll
    public static void destroy() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:src/test/resources/test.db")) {
            String sql = "DROP TABLE IF EXISTS PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void clearDB() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:src/test/resources/test.db")) {
            String sql = "DELETE FROM PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public void doGetNoExceptDefaultResponse(Consumer<StringWriter> test)
        throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        test.accept(stringWriter);

        Mockito.verify(response).setContentType("text/html");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
