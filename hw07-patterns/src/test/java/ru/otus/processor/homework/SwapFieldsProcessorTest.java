package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SwapFieldsProcessorTest {

    private Processor processor;

    @BeforeEach
    void setUp() {
        processor = new SwapFieldsProcessor();
    }

    @Test
    @DisplayName("Тестируем процессор перестановки полей field11 и field12")
    void swapField11andField12Test() {

        var field11 = "field11";
        var field12 = "field12";
        var message = new Message.Builder(1L).field11(field11).field12(field12).build();

        var processors = List.of(processor);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});

        var result = complexProcessor.handle(message);

        assertThat(result.getField11()).isEqualTo(field12);
        assertThat(result.getField12()).isEqualTo(field11);

    }
}