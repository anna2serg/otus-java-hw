package ru.otus.processor.homework.exception;

public class EvenSecException extends RuntimeException {

    private static final String message = "Exception time because the second is even";

    public EvenSecException() {
        super(message);
    }
}
