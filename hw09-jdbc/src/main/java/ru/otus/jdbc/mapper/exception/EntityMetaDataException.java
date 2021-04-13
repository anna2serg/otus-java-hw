package ru.otus.jdbc.mapper.exception;

public class EntityMetaDataException extends RuntimeException {
    public EntityMetaDataException(String message) {
        super(message);
    }

    public EntityMetaDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
