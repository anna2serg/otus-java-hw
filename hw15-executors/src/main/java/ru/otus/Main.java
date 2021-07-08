package ru.otus;

public class Main {

    private static final int THREADS_COUNT = 2;

    public static void main(String[] args) {
        var customSequence = new CustomSequence(1, 10, 1, THREADS_COUNT);
        var sequencePrinter = new SequencePrinter(customSequence);
        sequencePrinter.print(THREADS_COUNT);
    }
}
