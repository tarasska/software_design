package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.RealDBTest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;


public class BaseServletRealDBTest extends RealDBTest {
    @Mock
    protected HttpServletRequest request;

    @Mock
    protected HttpServletResponse response;

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    public void doGetNoExceptDefaultResponse(Consumer<StringWriter> test) throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        test.accept(stringWriter);

        Mockito.verify(response).setContentType("text/html");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
