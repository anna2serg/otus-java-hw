package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.exception.EvenSecException;
import ru.otus.processor.homework.provider.DateTimeProvider;

public class EvenSecExceptionProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public EvenSecExceptionProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        var dateTime = dateTimeProvider.getDateTime();
        var sec = dateTime.getSecond();
        if ((sec % 2) == 0) {
            System.out.println("even sec");
            throw new EvenSecException();
        } else {
            System.out.println("odd sec");
            return message.toBuilder().build();
        }
    }
}