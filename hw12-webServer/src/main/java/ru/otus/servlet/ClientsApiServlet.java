package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.service.DBServiceClient;

import java.io.IOException;
import java.util.stream.Collectors;

public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceClient clientService;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient clientService, Gson gson) {
        this.clientService = clientService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClientDataSet client = clientService.getClient(extractIdFromRequest(request)).orElse(null);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String clientToJson = gson.toJson(client);
        out.print(clientToJson);
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clientData = request.getReader().lines().collect(Collectors.joining());
        ClientDataSet newClient = gson.fromJson(clientData, ClientDataSet.class);
        clientService.saveClient(newClient);
    }


}
