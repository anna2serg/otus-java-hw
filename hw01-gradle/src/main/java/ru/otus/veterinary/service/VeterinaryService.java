package ru.otus.veterinary.service;

import ru.otus.veterinary.domain.AnimalPatient;

public interface VeterinaryService {

    void takeCareOf(AnimalPatient patient);
    void displayCareStat();

}
