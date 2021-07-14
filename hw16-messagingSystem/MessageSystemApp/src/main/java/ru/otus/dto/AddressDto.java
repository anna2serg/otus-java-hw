package ru.otus.dto;

import ru.otus.db.model.AddressDataSet;

public class AddressDto {

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
