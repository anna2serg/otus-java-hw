package ru.otus.atm.impl;

import ru.otus.atm.ATM;
import ru.otus.atm.Cassette;
import ru.otus.banknote.Combination;
import ru.otus.banknote.Combinator;
import ru.otus.banknote.impl.CombinatorImpl;
import ru.otus.banknote.Denomination;
import ru.otus.banknote.exception.UnavailableCountOfBanknotesException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ATMImpl implements ATM {

    private final int CASSETTES_CAPACITY = 6;
    private final List<Cassette> cassettes = new ArrayList<>();
    private final Combinator combinator = new CombinatorImpl();

    @Override
    public void setCombinationStrategy(Comparator<Combination> combinationStrategy) {
        combinator.setCombinationStrategy(combinationStrategy);
    }

    @Override
    public Combination dispenseCash(double amount) {
        updateAllAvailableCombinations();
        Combination combination = combinator.getCombinationFromAvailable(amount);
        pullCash(combination);
        return combination;
    }

    @Override
    public double getCashBalance() {
        return cassettes.stream()
                .map(Cassette::getBalance)
                .reduce(0.00, Double::sum);
    }

    @Override
    public void addCassette(Cassette cassette) {
        if (cassettes.size() > CASSETTES_CAPACITY) {
            throw new UnsupportedOperationException("The allowed number of cassettes has been exceeded");
        }
        cassettes.add(cassette);
        combinator.enrichAvailableCombination(cassette.getBanknoteDenomination(), cassette.getBanknoteCount());
    }

    @Override
    public void removeAllCassette() {
        cassettes.clear();
        combinator.resetAvailableCombination();
    }

    private void updateAllAvailableCombinations() {
        combinator.resetAvailableCombination();
        for (Cassette cassette : cassettes) {
            combinator.enrichAvailableCombination(cassette.getBanknoteDenomination(), cassette.getBanknoteCount());
        }
    }

    private void pullCash(Combination cashCombination) {
        Combination extractableCombination = new Combination(cashCombination);
        for (Cassette cassette : cassettes) {
            for (Denomination denomination : extractableCombination.getContainedDenominations()) {
                if (Objects.equals(cassette.getBanknoteDenomination(), denomination) && extractableCombination.getCountForDenomination(denomination) > 0) {
                    int extractedCount;
                    try {
                        extractedCount = cassette.pullBanknotes(extractableCombination.getCountForDenomination(denomination));
                    } catch (UnavailableCountOfBanknotesException ex) {
                        extractedCount = cassette.pullBanknotes(cassette.getBanknoteCount());
                    }
                    int remainingCount = extractableCombination.getCountForDenomination(denomination) - extractedCount;
                    extractableCombination.setCountForDenomination(denomination, remainingCount);
                }
            }
        }
        updateAllAvailableCombinations();
    }
}
