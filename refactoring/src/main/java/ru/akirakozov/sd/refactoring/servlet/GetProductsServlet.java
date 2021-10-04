package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractProductServlet {

    public GetProductsServlet(ProductDao productDao) {
        super(productDao);
    }

    @Override
    protected void htmlDoGetImpl(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        productDao.getProducts(response.getWriter());
    }
}
