package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.AddressDataSet;
import ru.otus.crm.model.ClientDataSet;
import ru.otus.crm.model.PhoneDataSet;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.util.HibernateUtils;

import java.util.Set;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) {

        var configuration = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5430/demoDB")
                .setProperty("hibernate.connection.username", "usr")
                .setProperty("hibernate.connection.password", "pwd")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "create")
                .setProperty("hibernate.generate_statistics", "true");

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                ClientDataSet.class,
                AddressDataSet.class,
                PhoneDataSet.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(ClientDataSet.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var client = new ClientDataSet("dbServiceFirst");

        var address = new AddressDataSet("dbServiceFirstStreet");
        client.setAddress(address);

        var phone1 = new PhoneDataSet("111-222");
        client.addPhone(phone1);

        var phone2 = new PhoneDataSet("333-444");
        client.addPhone(phone2);

        dbServiceClient.saveClient(client);

        var clientSecond = dbServiceClient.saveClient(
                new ClientDataSet("dbServiceSecond",
                        new AddressDataSet("dbServiceSecondStreet"),
                        Set.of(new PhoneDataSet("555-666"))
                )
        );

        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        clientSecondSelected.setName("dbServiceSecondUpdated");
        AddressDataSet clientSecondAddress = clientSecondSelected.getAddress();
        clientSecondAddress.setStreet("dbServiceSecondStreetUpdated");
        var removedPhone = clientSecondSelected.getPhones().iterator().next();
        clientSecondSelected.removePhone(removedPhone);
        clientSecondSelected.addPhone(new PhoneDataSet("777-888"));

        dbServiceClient.saveClient(clientSecondSelected);

        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(c -> log.info("client:{}", c));
    }
}
