package ru.otus.jdbc.mapper.exception;

public class EntityDataException extends RuntimeException {
    public EntityDataException(String message) {
        super(message);
    }

    public EntityDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
