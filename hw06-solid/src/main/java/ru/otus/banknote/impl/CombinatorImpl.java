package ru.otus.banknote.impl;

import java.util.*;
import java.util.stream.Collectors;

import ru.otus.banknote.Combination;
import ru.otus.banknote.Combinator;
import ru.otus.banknote.Denomination;
import ru.otus.banknote.exception.*;

public class CombinatorImpl implements Combinator {

    private final Comparator<Combination> defaultCombinationStrategy = (o1, o2) -> {
        List<Denomination> denominations = Arrays.stream(Denomination.values()).sorted(Collections.reverseOrder()).collect(Collectors.toList());
        for (Denomination denomination : denominations) {
            int denominationCount1 = o1.getCountForDenomination(denomination);
            int denominationCount2 = o2.getCountForDenomination(denomination);
            if (denominationCount1 == denominationCount2) {
                continue;
            } else {
                return denominationCount2 - denominationCount1;
            }
        }
        return 0;
    };

    private Combination availableCombination;
    private Comparator<Combination> combinationStrategy;

    public CombinatorImpl() {
        this.availableCombination = new Combination();
        this.combinationStrategy = defaultCombinationStrategy;
    }

    public CombinatorImpl(Comparator<Combination> combinationStrategy) {
        this();
        setCombinationStrategy(combinationStrategy);
    }

    @Override
    public void setCombinationStrategy(Comparator<Combination> combinationStrategy) {
        this.combinationStrategy = combinationStrategy;
    }

    @Override
    public void resetAvailableCombination() {
        availableCombination.reset();
    }

    @Override
    public void enrichAvailableCombination(Denomination denomination, int count) {
        availableCombination.add(denomination, count);
    }

    @Override
    public Combination getCombinationFromAvailable(double amount) {
        List<Combination> combinations = getAllCombinationsFromAvailable(amount);
        if (combinations.size() == 0) {
            throw new CombinationNotFoundException(amount);
        }
        return combinations.stream().sorted(combinationStrategy).iterator().next();
    }

    @Override
    public Combination getCombination(double amount) {
        List<Combination> combinations = getAllCombinations(amount);
        if (combinations.size() == 0) {
            throw new CombinationNotFoundException(amount);
        }
        return combinations.stream().sorted(combinationStrategy).iterator().next();
    }

    @Override
    public List<Combination> getAllCombinationsFromAvailable(double amount) {
        return getAllCombinationList(amount, availableCombination);
    }

    @Override
    public List<Combination> getAllCombinations(double amount) {
        return getAllCombinationList(amount, null);
    }

    private List<Combination> getAllCombinationList(double amount, Combination availableCombination) {
        List<Combination> combinationList = new ArrayList<>();
        populateCombinations(amount, new Combination(), combinationList, availableCombination);
        return combinationList;
    }

    private void populateCombinations(double amount, Combination combination, List<Combination> combinationList, Combination availableCombination) {

        Iterable<Denomination> denominations;
        if (Objects.isNull(availableCombination)) {
            denominations = Arrays.stream(Denomination.values()).collect(Collectors.toList());
        } else {
            denominations = availableCombination.getContainedDenominations();
        }

        denominations.forEach(denomination -> {
            int count = (int)(amount / denomination.getValue());
            double residue = amount % denomination.getValue();

            if (count == 0) {
                return;
            }

            if (Objects.nonNull(availableCombination)) {
                int availableCount = availableCombination.getCountForDenomination(denomination);
                if (count > availableCount) {
                    return;
                }
            }

            Combination newCombination = new Combination(combination);
            newCombination.add(denomination, count);

            if (residue == 0) {
                combinationList.add(newCombination);
            } else {
                populateCombinations(residue, newCombination, combinationList, availableCombination);
            }

        });
    }
}