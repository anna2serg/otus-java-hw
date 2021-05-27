package ru.otus.db.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.db.repository.DataTemplate;
import ru.otus.db.sessionmanager.TransactionManager;
import ru.otus.db.model.UserDataSet;

import java.util.Optional;

public class DBServiceUserImpl implements DBServiceUser {

    private static final Logger log = LoggerFactory.getLogger(DBServiceUserImpl.class);

    private final DataTemplate<UserDataSet> userDataTemplate;
    private final TransactionManager transactionManager;

    public DBServiceUserImpl(TransactionManager transactionManager, DataTemplate<UserDataSet> userDataTemplate) {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
    }

    @Override
    public Optional<UserDataSet> getByLogin(String login) {
        return transactionManager.doInTransaction(session -> {
            var userOptional = userDataTemplate.findByField(session, "login", login);
            log.info("user: {}", userOptional);
            return userOptional;
        });
    }
}
