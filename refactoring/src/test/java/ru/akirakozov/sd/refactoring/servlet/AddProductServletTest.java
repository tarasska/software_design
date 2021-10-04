package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.database.DBManagerImpl;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class AddProductServletTest extends BaseServletRealDBTest {

    @Test
    public void doGetNoExcept() throws IOException {
        super.doGetNoExceptDefaultResponse((StringWriter writer) -> {
            try {
                Mockito.when(request.getParameter("name")).thenReturn("iphone6");
                Mockito.when(request.getParameter("price")).thenReturn("30000");

                new AddProductServlet(
                    new ProductDao(new DBManagerImpl("jdbc:sqlite:src/test/resources/test.db"))
                ).doGet(request, response);
                assertTrue(writer.toString().contains("OK"));
            } catch (IOException e) {
                fail("Unexpected exception", e);
            }
        });
    }

}
