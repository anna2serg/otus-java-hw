package ru.otus.db.service;

import ru.otus.db.model.ClientDataSet;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    ClientDataSet saveClient(ClientDataSet clientDataSet);

    Optional<ClientDataSet> getClient(long id);

    List<ClientDataSet> findAll();
}
