package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.db.model.AddressDataSet;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.model.PhoneDataSet;
import ru.otus.db.service.DBServiceClient;
import ru.otus.service.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;

public class AddClientServlet extends HttpServlet {

    private static final String ADD_CLIENT_PAGE_TEMPLATE = "addClient.html";

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient clientService;

    public AddClientServlet(TemplateProcessor templateProcessor, DBServiceClient clientService) {
        this.templateProcessor = templateProcessor;
        this.clientService = clientService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(ADD_CLIENT_PAGE_TEMPLATE, null));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clientName = request.getParameter("client_name");
        String clientPhone = request.getParameter("client_phone");
        String clientAddress = request.getParameter("client_address");

        var newClient = new ClientDataSet(clientName, new AddressDataSet(clientAddress), Collections.singleton(new PhoneDataSet(clientPhone)));
        clientService.saveClient(newClient);

        String path = request.getContextPath() + ServletPaths.CLIENT_LIST;
        response.sendRedirect(path);
    }
}
