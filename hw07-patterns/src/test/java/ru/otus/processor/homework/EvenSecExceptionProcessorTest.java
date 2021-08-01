package ru.otus.processor.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.handler.ComplexProcessor;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.provider.DateTimeProvider;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EvenSecExceptionProcessorTest {

    public static final LocalDateTime TEST_DATETIME_WITH_EVEN_SEC = LocalDateTime.of(2021, 8, 1, 18, 0, 50);
    public static final LocalDateTime TEST_DATETIME_WITH_ODD_SEC = LocalDateTime.of(2021, 8, 1, 18, 0, 51);

    private DateTimeProvider dateTimeProvider;
    private Processor processor;

    @BeforeEach
    void setUp() {
        dateTimeProvider = Mockito.mock(DateTimeProvider.class);
        processor = new EvenSecExceptionProcessor(dateTimeProvider);
    }

    @Test
    @DisplayName("Тестируем проброс исключения при обработке в четную секунду")
    void throwExceptionWhenEvenSecTest() {

        Mockito.when(dateTimeProvider.getDateTime()).thenReturn(TEST_DATETIME_WITH_EVEN_SEC);
        var message = new Message.Builder(1L).field5("field5").build();

        var processors = List.of(processor);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new EvenSecExceptionProcessorTest.TestException(ex.getMessage(), ex);
        });

        Assertions.assertThrows(EvenSecExceptionProcessorTest.TestException.class,
                () -> complexProcessor.handle(message));

    }

    @Test
    @DisplayName("Тестируем отсутствие исключения при обработке в нечетную секунду")
    void noExceptionWhenOddSecTest() {

        Mockito.when(dateTimeProvider.getDateTime()).thenReturn(TEST_DATETIME_WITH_ODD_SEC);

        var field13 = new ObjectForMessage();
        field13.setData(List.of("field13_1", "field13_2"));
        var message = new Message.Builder(1L).field5("field5").field13(field13).build();

        var processors = List.of(processor);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new EvenSecExceptionProcessorTest.TestException(ex.getMessage(), ex);
        });

        var result = complexProcessor.handle(message);

        assertThat(result).isEqualTo(message);

    }

    private static class TestException extends RuntimeException {
        public TestException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}