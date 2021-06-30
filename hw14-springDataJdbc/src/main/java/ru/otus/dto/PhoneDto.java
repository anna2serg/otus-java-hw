package ru.otus.dto;

import ru.otus.db.model.PhoneDataSet;

public class PhoneDto {

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
