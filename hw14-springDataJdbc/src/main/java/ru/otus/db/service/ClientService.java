package ru.otus.db.service;

import ru.otus.db.model.ClientDataSet;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    ClientDataSet save(ClientDataSet clientDataSet);

    Optional<ClientDataSet> get(long id);

    List<ClientDataSet> findAll();
}
