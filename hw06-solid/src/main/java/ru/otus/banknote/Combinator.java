package ru.otus.banknote;

import java.util.Comparator;
import java.util.List;

public interface Combinator {

    void setCombinationStrategy(Comparator<Combination> combinationStrategy);

    Combination getCombinationFromAvailable(double amount);

    Combination getCombination(double amount);

    List<Combination> getAllCombinationsFromAvailable(double amount);

    List<Combination> getAllCombinations(double amount);

    void resetAvailableCombination();

    void enrichAvailableCombination(Denomination denomination, int count);

}
