package ru.otus.banknote.exception;

public class CombinationNotFoundException extends RuntimeException {

    private static final String message = "Combination for the amount %.2f rub. not found";

    public CombinationNotFoundException(Double amount) {
        super(String.format(message, amount));
    }

}