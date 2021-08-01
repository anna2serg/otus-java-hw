package ru.otus.listener.homework;

import ru.otus.model.Message;

import java.util.List;
import java.util.Optional;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);

    List<Message> getHistoryById(long id);
}
