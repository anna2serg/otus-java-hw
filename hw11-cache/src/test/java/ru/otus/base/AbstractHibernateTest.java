package ru.otus.base;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.AddressDataSet;
import ru.otus.crm.model.ClientDataSet;
import ru.otus.crm.model.PhoneDataSet;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.util.HibernateUtils;

public abstract class AbstractHibernateTest {

    private static TestContainersConfig.CustomPostgreSQLContainer CONTAINER;
    protected SessionFactory sessionFactory;
    protected TransactionManagerHibernate transactionManager;
    protected DataTemplate<ClientDataSet> clientDataTemplate;
    protected HwCache<String, ClientDataSet> clientCache;
    protected DBServiceClient dbServiceClient;

    @BeforeAll
    public static void init() {
        CONTAINER = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
        CONTAINER.start();
    }

    @BeforeEach
    public void setUp() {
        String dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
        String dbUserName = System.getProperty("app.datasource.demo-db.username");
        String dbPassword = System.getProperty("app.datasource.demo-db.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var configuration = new Configuration()
                .setProperty("hibernate.connection.url", dbUrl)
                .setProperty("hibernate.connection.username", dbUserName)
                .setProperty("hibernate.connection.password", dbPassword)
                .setProperty("hibernate.hbm2ddl.auto", "validate");

        sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                ClientDataSet.class,
                AddressDataSet.class,
                PhoneDataSet.class);

        transactionManager = new TransactionManagerHibernate(sessionFactory);
        clientDataTemplate = new DataTemplateHibernate<>(ClientDataSet.class);
        clientCache = new MyCache<>();
        dbServiceClient = new DbServiceClientImpl(transactionManager, clientDataTemplate, clientCache);
    }

    @AfterAll
    public static void shutdown() {
        CONTAINER.stop();
    }

}
