package ru.otus.veterinary.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimalPatient {
    String name;
    Integer age;
    AnimalKind kind;
    String conclusion;
}