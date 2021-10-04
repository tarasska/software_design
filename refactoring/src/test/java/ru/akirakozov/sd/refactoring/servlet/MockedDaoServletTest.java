package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.BaseTest;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.entities.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockedDaoServletTest extends BaseTest {
    @Mock
    private ProductDao dao;

    @Mock
    protected HttpServletRequest request;

    @Mock
    protected HttpServletResponse response;

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addProductServlet() {
        when(request.getParameter(nameColumn)).thenReturn("item");
        when(request.getParameter(priceColumn)).thenReturn("100");

        testWithWriter((stringWriter, printWriter) -> {
            when(response.getWriter()).thenReturn(printWriter);

            new AddProductServlet(dao).doGet(request, response);

            verify(dao).addProduct(new Product("item", 100));
        });
    }

    @Test
    public void getProductsServlet() {
        testWithWriter((stringWriter, printWriter) -> {
            when(response.getWriter()).thenReturn(printWriter);

            new GetProductsServlet(dao).doGet(request, response);

            verify(dao).getProducts(printWriter);
        });
    }

    @Test
    public void queryServlet() {
        testWithWriter((stringWriter, printWriter) -> {
            when(response.getWriter()).thenReturn(printWriter);

            List<String> commands = List.of("min", "max", "count", "sum", "unknown");

            QueryServlet servlet = new QueryServlet(dao);

            for (String cmd : commands) {
                when(request.getParameter("command")).thenReturn(cmd);
                servlet.doGet(request, response);
                switch (cmd) {
                    case "min":
                        verify(dao).findProductsMinPrice(printWriter);
                        break;
                    case "max":
                        verify(dao).findProductsMaxPrice(printWriter);
                        break;
                    case "count":
                        verify(dao).countProducts(printWriter);
                        break;
                    case "sum":
                        verify(dao).sumProductsPrice(printWriter);
                        break;
                    default:
                        assertEquals("Unknown command: unknown" + newLine, stringWriter.toString());
                }
            }
        });
    }
}
