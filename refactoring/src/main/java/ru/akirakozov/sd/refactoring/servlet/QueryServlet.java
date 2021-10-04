package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractProductServlet {

    public QueryServlet(ProductDao productDao) {
        super(productDao);
    }

    @Override
    protected void htmlDoGetImpl(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String command = request.getParameter("command");

        switch (request.getParameter("command")) {
            case "max":
                productDao.findProductsMaxPrice(response.getWriter());
                break;
            case "min":
                productDao.findProductsMinPrice(response.getWriter());
                break;
            case "sum":
                productDao.sumProductsPrice(response.getWriter());
                break;
            case "count":
                productDao.countProducts(response.getWriter());
                break;
            default:
                response.getWriter().println("Unknown command: " + command);
                break;
        }
    }
}
