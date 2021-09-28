package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

public class QueryServletTest extends BaseServletRealDBTest {

    public void queryTest(String command, String expectedHeaderContent) throws IOException {
        super.doGetNoExceptDefaultResponse((StringWriter writer) -> {
            try {
                Mockito.when(request.getParameter("command")).thenReturn(command);

                new QueryServlet().doGet(request, response);

                String expectedLogResult
                    = "<html><body>" + System.lineSeparator()
                    + expectedHeaderContent + System.lineSeparator()
                    + "</body></html>" + System.lineSeparator();

                assertEquals(expectedLogResult, writer.toString());
            } catch (IOException e) {
                fail("Unexpected exception", e);
            }
        });
    }

    @Test
    public void doGetMax() throws IOException {
        queryTest("max", "<h1>Product with max price: </h1>");
    }

    @Test
    public void doGetMin() throws IOException {
        queryTest("min", "<h1>Product with min price: </h1>");
    }

    @Test
    public void doGetSum() throws IOException {
        queryTest("sum", "Summary price: " + System.lineSeparator() + "0");
    }

    @Test
    public void doGetCount() throws IOException {
        queryTest("count", "Number of products: " + System.lineSeparator() + "0");
    }
}
