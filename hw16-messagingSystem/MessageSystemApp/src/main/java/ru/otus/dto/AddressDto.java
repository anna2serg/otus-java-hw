package ru.otus.dto;

import ru.otus.db.model.AddressDataSet;
import ru.otus.messagesystem.client.ResultDataType;

public class AddressDto extends ResultDataType {

    private String street;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public AddressDataSet toModel() {
        return new AddressDataSet(getStreet());
    }
}
