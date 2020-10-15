package ru.otus.veterinary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnimalKind {
    CAT("кошка"),
    DOG("собака"),
    TURTLE("черепаха"),
    RABBIT("кролик"),
    OTHER("иное");

    private String kind;

    @Override
    public String toString() {
        return this.kind;
    }
}
