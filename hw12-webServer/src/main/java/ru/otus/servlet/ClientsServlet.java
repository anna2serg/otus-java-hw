package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.db.service.DBServiceClient;
import ru.otus.service.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static ru.otus.server.ClientWebServerWithBasicSecurity.ROLE_NAME_ADMIN;

public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";
    private static final String TEMPLATE_ATTR_IS_ADMIN = "isAdmin";

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient clientService;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient clientService) {
        this.templateProcessor = templateProcessor;
        this.clientService = clientService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, clientService.findAll());
        paramsMap.put(TEMPLATE_ATTR_IS_ADMIN, request.isUserInRole(ROLE_NAME_ADMIN));
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

}
