package ru.akirakozov.sd.refactoring.dao;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.RealDBTest;
import ru.akirakozov.sd.refactoring.database.DBManager;
import ru.akirakozov.sd.refactoring.database.DBManagerImpl;
import ru.akirakozov.sd.refactoring.entities.Product;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoTest extends RealDBTest {
    @FunctionalInterface
    interface CheckedBiConsumer<T1, T2> {
        void accept(T1 t1, T2 t2) throws SQLException;
    }

    private final DBManager manager = new DBManagerImpl(TEST_DB_URL);
    private final ProductDao dao = new ProductDao(manager);
    private final String nameColumn = "name";
    private final String priceColumn = "price";
    private final String newLine = System.lineSeparator();

    private String buildDefaultHtml(String body) {
        return "<html><body>" + newLine
            + (body != null ? body + newLine : "")
            + "</body></html>" + newLine;
    }

    private String buildProductHtmlRow(Product product) {
        return product.getName() + "\t" + product.getPrice() + "</br>";
    }

    private void clearStringWriter(StringWriter writer) {
        writer.getBuffer().setLength(0);
    }

    private void testWithWriter(CheckedBiConsumer<StringWriter, PrintWriter> test) {
        assertDoesNotThrow(() -> {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);

            test.accept(stringWriter, printWriter);
        });
    }


    @Test
    public void addTest() {
        assertDoesNotThrow(() -> {
            dao.addProduct(new Product("iphone6", 99999));
            dao.addProduct(new Product("yotaphone", 29999));

            manager.executeQuery(statement -> {
                try (ResultSet rs = statement.executeQuery("SELECT * FROM PRODUCT")) {

                    while (rs.next()) {
                        switch (rs.getString(nameColumn)) {
                            case "iphone6":
                                assertEquals(99999, rs.getInt(priceColumn));
                                break;
                            case "yotaphone":
                                assertEquals(29999, rs.getInt(priceColumn));
                                break;
                            default: fail("Unexpected item in database");
                        }
                    }
                }
            });
        });
    }

    @Test
    public void getEmptyTest() {
        testWithWriter((stringWriter, printWriter) -> {
            String emptyAnswer = buildDefaultHtml(null);
            dao.getProducts(printWriter);
            assertEquals(emptyAnswer, stringWriter.toString());
        });
    }

    @Test
    public void getAddTest() {
        testWithWriter((stringWriter, printWriter) -> {
            Product item1 = new Product("item1", 1);
            Product item2 = new Product("item2", 2);
            dao.addProduct(item1);
            dao.addProduct(item2);
            dao.getProducts(printWriter);

            String productsHtml = buildDefaultHtml(
                buildProductHtmlRow(item1) + newLine +
                    buildProductHtmlRow(item2)
            );
            assertEquals(productsHtml, stringWriter.toString());
        });
    }

    @Test
    public void maxPriceTest() {
        testWithWriter((stringWriter, printWriter) -> {
            List<Product> products = List.of(
                new Product("item1", 999),
                new Product("item2", 1),
                new Product("item3", 10000)
            );
            for (Product product : products) {
                dao.addProduct(product);
            }
            dao.findProductsMaxPrice(printWriter);
            String maxPriceHtml = buildDefaultHtml(
                "<h1>Product with max price: </h1>" + newLine +
                    buildProductHtmlRow(products.get(2))
            );
            assertEquals(maxPriceHtml, stringWriter.toString());
        });
    }

    @Test
    public void minPriceTest() {
        testWithWriter((stringWriter, printWriter) -> {
            List<Product> products = List.of(
                new Product("item1", 123456),
                new Product("item2", 0),
                new Product("item3", 1)
            );
            for (Product product : products) {
                dao.addProduct(product);
            }
            dao.findProductsMinPrice(printWriter);
            String minPriceHtml = buildDefaultHtml(
                "<h1>Product with min price: </h1>" + newLine +
                    buildProductHtmlRow(products.get(1))
            );
            assertEquals(minPriceHtml, stringWriter.toString());
        });
    }

    @Test
    public void countTest() {
        testWithWriter((stringWriter, printWriter) -> {
            for (int i = 0; i < 50; i++) {
                dao.addProduct(new Product("item" + i, i));
            }
            dao.countProducts(printWriter);
            String countHtml = buildDefaultHtml("Number of products: " + newLine + 50);
            assertEquals(countHtml, stringWriter.toString());
        });
    }

    @Test
    public void sumTest() {
        testWithWriter((stringWriter, printWriter) -> {
            for (int i = 0; i < 10; i++) {
                dao.addProduct(new Product("item" + i, i));
            }
            int expectedSum = 45;
            dao.sumProductsPrice(printWriter);
            String sumHtml = buildDefaultHtml("Summary price: " + newLine + expectedSum);
            assertEquals(sumHtml, stringWriter.toString());
        });
    }
}
