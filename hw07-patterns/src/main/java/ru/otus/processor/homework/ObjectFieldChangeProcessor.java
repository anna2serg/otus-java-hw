package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.util.List;
import java.util.Objects;

public class ObjectFieldChangeProcessor implements Processor {

    @Override
    public Message process(Message message) {
        var field13 = message.getField13();
        var newField13 = field13.clone();
        if (Objects.nonNull(newField13)) {
            List<String> newField13data = newField13.getData();
            newField13data.add("bug");
            newField13.setData(newField13data);
        }
        return message.toBuilder().field13(newField13).build();
    }
}