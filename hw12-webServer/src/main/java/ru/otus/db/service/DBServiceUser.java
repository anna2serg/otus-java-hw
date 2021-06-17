package ru.otus.db.service;

import ru.otus.db.model.UserDataSet;

import java.util.Optional;

public interface DBServiceUser {

    Optional<UserDataSet> getByLogin(String login);

}
