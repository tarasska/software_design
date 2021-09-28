package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;


public class AddProductServletTest extends BaseServletRealDBTest {

    @Test
    public void doGetNoExcept() throws IOException {
        super.doGetNoExceptDefaultResponse(() -> {
            try {
                Mockito.when(request.getParameter("name")).thenReturn("iphone6");
                Mockito.when(request.getParameter("price")).thenReturn("30000");

                new AddProductServlet().doGet(request, response);
            } catch (IOException e) {
                fail("Unexpected exception", e);
            }
        });
    }

}
