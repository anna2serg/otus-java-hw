package ru.otus.listener.homework.history;

import ru.otus.model.Message;

import java.util.List;
import java.util.Optional;

public interface MessageHistory {

    void saveMessage(Message message);
    Optional<Message> getMessage();
    List<Message> getHistory();

}
