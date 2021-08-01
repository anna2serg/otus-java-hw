package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinter;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.EvenSecExceptionProcessor;
import ru.otus.processor.homework.ObjectFieldChangeProcessor;
import ru.otus.processor.homework.SwapFieldsProcessor;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {

    public static void main(String[] args) {

        var messageId = 1L;

        List<Processor> processors = List.of(
                new SwapFieldsProcessor(),
                new ObjectFieldChangeProcessor(),
                new EvenSecExceptionProcessor(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, System.out::println);

        var listenerPrinter = new ListenerPrinter();
        complexProcessor.addListener(listenerPrinter);

        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        var field13 = new ObjectForMessage();
        field13.setData(List.of("field13_1", "field13_2"));
        var message = new Message.Builder(messageId)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        var result = complexProcessor.handle(message);

        System.out.println("input message: " + message);
        System.out.println("result message: " + result);

        System.out.println("message history:");
        historyListener.getHistoryById(messageId).forEach(System.out::println);

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(historyListener);

    }
}
