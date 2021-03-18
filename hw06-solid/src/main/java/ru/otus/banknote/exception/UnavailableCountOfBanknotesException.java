package ru.otus.banknote.exception;

public class UnavailableCountOfBanknotesException extends RuntimeException {

    private static final String message = "The requested number of banknotes is not available: requested = %d, available = %d";

    public UnavailableCountOfBanknotesException(int requestedCount, int availableCount) {
        super(String.format(message, requestedCount, availableCount));
    }

}