package ru.otus.crm.service;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.crm.model.ClientDataSet;
import ru.otus.core.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private HwCache<String, ClientDataSet> cache;

    private final DataTemplate<ClientDataSet> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(TransactionManager transactionManager,
                               DataTemplate<ClientDataSet> clientDataTemplate,
                               HwCache<String, ClientDataSet> cache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public ClientDataSet saveClient(ClientDataSet clientDataSet) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = clientDataSet.clone();
            if (clientDataSet.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                cache.put(String.valueOf(clientCloned.getId()), clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            cache.put(String.valueOf(clientCloned.getId()), clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<ClientDataSet> getClient(long id) {
        return transactionManager.doInTransaction(session -> {
            var clientOptional = getClientThroughCache(session, id);
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

    private Optional<ClientDataSet> getClientThroughCache(Session session, long id) {
        String key = String.valueOf(id);
        ClientDataSet client = cache.get(key);
        if (Objects.nonNull(client)) {
            return Optional.of(client);
        }
        var clientOptional = clientDataTemplate.findById(session, id);
        if (clientOptional.isPresent()) {
            client = clientOptional.get();
            cache.put(key, client);
        }
        return clientOptional;
    }
}
