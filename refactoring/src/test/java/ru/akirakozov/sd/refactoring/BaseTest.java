package ru.akirakozov.sd.refactoring;

import ru.akirakozov.sd.refactoring.entities.Product;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BaseTest {
    @FunctionalInterface
    protected interface CheckedBiConsumer<T1, T2> {
        void accept(T1 t1, T2 t2) throws SQLException, IOException;
    }

    protected final String nameColumn = "name";
    protected final String priceColumn = "price";
    protected final String newLine = System.lineSeparator();

    protected String buildDefaultHtml(String body) {
        return "<html><body>" + newLine
            + (body != null ? body + newLine : "")
            + "</body></html>" + newLine;
    }

    protected String buildProductHtmlRow(Product product) {
        return product.getName() + "\t" + product.getPrice() + "</br>";
    }

    protected void testWithWriter(CheckedBiConsumer<StringWriter, PrintWriter> test) {
        assertDoesNotThrow(() -> {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);

            test.accept(stringWriter, printWriter);
        });
    }

}
