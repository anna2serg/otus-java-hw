package ru.otus.dto;

import org.springframework.lang.Nullable;
import ru.otus.messagesystem.client.ResultDataType;

public class MessageDto<T> extends ResultDataType {

    private final MessageStatus status;

    @Nullable
    private final T result;

    @Nullable
    private final String message;


    public MessageDto(MessageStatus status, @Nullable T result, @Nullable String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public MessageStatus getStatus() {
        return status;
    }

    @Nullable
    public T getResult() {
        return result;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
