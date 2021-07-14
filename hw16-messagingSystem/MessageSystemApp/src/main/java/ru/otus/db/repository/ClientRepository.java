package ru.otus.db.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.db.model.ClientDataSet;

public interface ClientRepository extends CrudRepository<ClientDataSet, Long> {
}
