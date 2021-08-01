package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.listener.homework.history.MessageHistory;
import ru.otus.listener.homework.history.MessageHistoryKeeper;
import ru.otus.model.Message;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, MessageHistory> history = new HashMap<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        MessageHistory messageHistory = history.get(oldMsg.getId());
        if (Objects.isNull(messageHistory)) {
            messageHistory = new MessageHistoryKeeper();
            messageHistory.saveMessage(oldMsg.clone());
        }
        messageHistory.saveMessage(newMsg.clone());
        history.put(oldMsg.getId(), messageHistory);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        MessageHistory messageHistory = history.get(id);
        if (Objects.isNull(messageHistory)) {
            return Optional.empty();
        }
        return messageHistory.getMessage();
    }

    @Override
    public List<Message> getHistoryById(long id) {
        MessageHistory messageHistory = history.get(id);
        if (Objects.isNull(messageHistory)) {
            return null;
        }
        return messageHistory.getHistory();
    }
}
