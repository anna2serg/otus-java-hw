package ru.otus.dto;

import ru.otus.db.model.PhoneDataSet;
import ru.otus.messagesystem.client.ResultDataType;

public class PhoneDto extends ResultDataType {

    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public PhoneDataSet toModel() {
        return new PhoneDataSet(getNumber());
    }
}
