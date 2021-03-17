package ru.otus.banknote;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Denomination {

    ONE_HUNDRED(100.00),
    TWO_HUNDRED(200.00),
    FIVE_HUNDRED(500.00),
    ONE_THOUSAND(1000.00),
    TWO_THOUSAND(2000.00),
    FIVE_THOUSAND(5000.00);

    private double value;

}
