package ru.otus.listener.homework.history;

import ru.otus.model.Message;

import java.util.*;
import java.util.stream.Collectors;

public class MessageHistoryKeeper implements MessageHistory {

    private final Queue<Message> data = new ArrayDeque<>();

    @Override
    public void saveMessage(Message message) {
        Message messageToHistory = message.clone();
        data.add(messageToHistory);
    }

    @Override
    public Optional<Message> getMessage() {
        var messageInHistory = data.peek();
        if (Objects.isNull(messageInHistory)) {
            return Optional.empty();
        }
        return Optional.of(messageInHistory);
    }

    @Override
    public List<Message> getHistory() {
        return data.stream().collect(Collectors.toUnmodifiableList());
    }

}
