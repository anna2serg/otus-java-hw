package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.cfg.Configuration;
import ru.otus.db.model.*;
import ru.otus.db.repository.DataTemplateHibernate;
import ru.otus.db.service.DBServiceClient;
import ru.otus.db.service.DBServiceClientImpl;
import ru.otus.db.service.DBServiceUser;
import ru.otus.db.service.DBServiceUserImpl;
import ru.otus.db.sessionmanager.TransactionManager;
import ru.otus.db.sessionmanager.TransactionManagerHibernate;
import ru.otus.db.migrations.MigrationsExecutorFlyway;
import ru.otus.server.ClientWebServer;
import ru.otus.server.ClientWebServerWithBasicSecurity;
import ru.otus.service.LoginServiceImpl;
import ru.otus.service.TemplateProcessor;
import ru.otus.service.TemplateProcessorImpl;
import ru.otus.db.util.HibernateUtils;

public class WebServerWithBasicSecurityDemo {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String DB_CONNECTION_STRING = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String DB_USER_NAME = "usr";
    private static final String DB_USER_PASSWORD = "pwd";

    public static void main(String[] args) throws Exception {

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        var transactionManager = createTransactionManager();
        var dbServiceUser = createDBServiceUser(transactionManager);
        var dbServiceClient = createDBServiceClient(transactionManager);

        LoginService loginService = new LoginServiceImpl(dbServiceUser);

        ClientWebServer clientWebServer = new ClientWebServerWithBasicSecurity(WEB_SERVER_PORT,
                loginService, dbServiceUser, dbServiceClient, gson, templateProcessor);

        clientWebServer.start();
        clientWebServer.join();

    }

    private static TransactionManagerHibernate createTransactionManager() {

        new MigrationsExecutorFlyway(DB_CONNECTION_STRING, DB_USER_NAME, DB_USER_PASSWORD).executeMigrations();

        var configuration = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect")
                .setProperty("hibernate.connection.url", DB_CONNECTION_STRING)
                .setProperty("hibernate.connection.username", DB_USER_NAME)
                .setProperty("hibernate.connection.password", DB_USER_PASSWORD)
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "validate")
                .setProperty("hibernate.generate_statistics", "true");

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                UserDataSet.class,
                ClientDataSet.class,
                AddressDataSet.class,
                PhoneDataSet.class,
                RoleDataSet.class);

        return new TransactionManagerHibernate(sessionFactory);

    }

    private static DBServiceUser createDBServiceUser(TransactionManager transactionManager) {
        var userTemplate = new DataTemplateHibernate<>(UserDataSet.class);
        return new DBServiceUserImpl(transactionManager, userTemplate);
    }

    private static DBServiceClient createDBServiceClient(TransactionManager transactionManager) {
        var clientTemplate = new DataTemplateHibernate<>(ClientDataSet.class);
        return new DBServiceClientImpl(transactionManager, clientTemplate);
    }
}
