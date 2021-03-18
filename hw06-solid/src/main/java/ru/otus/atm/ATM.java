package ru.otus.atm;

import ru.otus.banknote.Combination;

import java.util.Comparator;

public interface ATM {

    void setCombinationStrategy(Comparator<Combination> combinationStrategy);

    Combination dispenseCash(double amount);

    double getCashBalance();

    void addCassette(Cassette cassette);

    void removeAllCassette();

}