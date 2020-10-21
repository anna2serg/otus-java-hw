package ru.otus.veterinary;

import ru.otus.veterinary.domain.AnimalKind;
import ru.otus.veterinary.domain.AnimalPatient;
import ru.otus.veterinary.service.VeterinaryService;
import ru.otus.veterinary.service.impl.VeterinaryServiceImpl;

/*
 * java -jar hw01-gradle.jar
 * java -jar vetStatApp-0.1.jar
 * */
public class HelloOtus {

    public static void main(String[] args) {

        VeterinaryService vetService = new VeterinaryServiceImpl();

        vetService.takeCareOf(AnimalPatient.builder()
                .name("Барсик")
                .age(5)
                .kind(AnimalKind.CAT)
                .conclusion("Мочекаменная")
                .build());

        vetService.takeCareOf(AnimalPatient.builder()
                .name("Бандит")
                .age(1)
                .kind(AnimalKind.DOG)
                .conclusion("Клещ")
                .build());

        vetService.takeCareOf(AnimalPatient.builder()
                .name("Элли")
                .age(99)
                .kind(AnimalKind.TURTLE)
                .conclusion("Конъюнктивит")
                .build());

        vetService.takeCareOf(AnimalPatient.builder()
                .name("Пантера")
                .age(10)
                .kind(AnimalKind.OTHER)
                .conclusion("Грипп")
                .build());

        vetService.takeCareOf(AnimalPatient.builder()
                .name("Зорк")
                .age(2)
                .kind(AnimalKind.DOG)
                .conclusion("Трахеобронхит")
                .build());

        vetService.takeCareOf(AnimalPatient.builder()
                .name("Елви")
                .age(3)
                .kind(AnimalKind.CAT)
                .conclusion("Отит")
                .build());

        vetService.takeCareOf(AnimalPatient.builder()
                .name("Томас")
                .age(8)
                .kind(AnimalKind.CAT)
                .conclusion("Аллергия")
                .build());

        vetService.displayCareStat();
    }

}