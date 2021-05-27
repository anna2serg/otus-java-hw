package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.db.service.DBServiceClient;
import ru.otus.db.service.DBServiceUser;
import ru.otus.service.TemplateProcessor;
import ru.otus.servlet.*;
import ru.otus.util.FileSystemHelper;

public class ClientWebServerSimple implements ClientWebServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final DBServiceUser userService;
    private final DBServiceClient clientService;
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public ClientWebServerSimple(int port, DBServiceUser userService, DBServiceClient clientService, Gson gson, TemplateProcessor templateProcessor) {
        this.userService = userService;
        this.clientService = clientService;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    public void init() {

    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler,
                ServletPaths.CLIENT_LIST.getPath(),
                ServletPaths.ADD_CLIENT.getPath(),
                ServletPaths.API_CLIENT.getPath()));

        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String ...paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, clientService)), ServletPaths.CLIENT_LIST.getPath());
        servletContextHandler.addServlet(new ServletHolder(new AddClientServlet(templateProcessor, clientService)), ServletPaths.ADD_CLIENT.getPath());
        servletContextHandler.addServlet(new ServletHolder(new ClientsApiServlet(clientService, gson)), ServletPaths.API_CLIENT.getPath());
        return servletContextHandler;
    }
}
