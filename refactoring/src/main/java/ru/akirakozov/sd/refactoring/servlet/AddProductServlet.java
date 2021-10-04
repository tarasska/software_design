package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.entities.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractProductServlet {

    public AddProductServlet(ProductDao productDao) {
        super(productDao);
    }

    @Override
    protected void htmlDoGetImpl(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        productDao.addProduct(new Product(name, price));

        response.getWriter().println("OK");
    }
}
