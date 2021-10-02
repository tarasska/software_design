package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public abstract class AbstractProductServlet extends HttpServlet {
    protected final ProductDao productDao;

    public AbstractProductServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            htmlDoGetImpl(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected abstract void htmlDoGetImpl(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException;
}
