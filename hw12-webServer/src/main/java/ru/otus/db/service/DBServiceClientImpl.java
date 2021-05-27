package ru.otus.db.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.db.repository.DataTemplate;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;

public class DBServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DBServiceClientImpl.class);

    private final DataTemplate<ClientDataSet> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DBServiceClientImpl(TransactionManager transactionManager,
                               DataTemplate<ClientDataSet> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public ClientDataSet saveClient(ClientDataSet clientDataSet) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = clientDataSet.clone();
            if (clientDataSet.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<ClientDataSet> getClient(long id) {
        return transactionManager.doInTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<ClientDataSet> findAll() {
        return transactionManager.doInTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}
