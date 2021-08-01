package ru.otus.listener.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class HistoryListenerTest {

    public static final long ORIGINAL_MESSAGE_ID = 100L;
    public static final String ORIGINAL_MESSAGE_DATA = "33";

    private HistoryListener historyListener;
    private Message messageA;
    private Message messageB;

    @BeforeEach
    void setUp() {
        historyListener = new HistoryListener();
        var field13 = new ObjectForMessage();
        field13.setData(List.of(ORIGINAL_MESSAGE_DATA));

        messageA = new Message.Builder(ORIGINAL_MESSAGE_ID)
                .field10("field10")
                .field13(field13)
                .build();

        messageB = new Message.Builder(12L)
                .field10("field10")
                .build();
    }

    @Test
    @DisplayName("Тестируем, что изменение исходного сообщения после сохранения истории не переписывает историю")
    void changingOriginalMessageDoesNotOverwriteHistoryTest() {

        //when
        historyListener.onUpdated(messageA, messageB);
        messageA.getField13().setData(new ArrayList<>()); //меняем исходное сообщение

        //then
        var messageFromHistory = historyListener.findMessageById(ORIGINAL_MESSAGE_ID);
        assertThat(messageFromHistory).isPresent();
        assertThat(messageFromHistory.get().getField13().getData()).containsExactly(ORIGINAL_MESSAGE_DATA);
    }

    @Test
    @DisplayName("Тестируем, что история содержит исходное и новое сообщение")
    void historyContainsOriginalAndModifiedMessageTest() {

        //when
        historyListener.onUpdated(messageA, messageB);

        //then
        var messageHistory = historyListener.getHistoryById(ORIGINAL_MESSAGE_ID);
        assertThat(messageHistory).isEqualTo(List.of(messageA, messageB));
    }

}