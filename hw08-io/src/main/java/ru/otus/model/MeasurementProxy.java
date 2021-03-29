package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MeasurementProxy {

    private Measurement measurement;

    @JsonCreator
    public MeasurementProxy(@JsonProperty("name") String name, @JsonProperty("value") double value) {
        this.measurement = new Measurement(name, value);
    }

    public Measurement getMeasurement() {
        return this.measurement;
    }

}
