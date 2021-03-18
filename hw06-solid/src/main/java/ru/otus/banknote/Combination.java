package ru.otus.banknote;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Combination {

    private final Map<Denomination, Integer> data = new TreeMap<>(Collections.reverseOrder());

    public Combination() {
    }

    public Combination(Combination other) {
        data.clear();
        data.putAll(other.data);
    }

    public void reset() {
        data.clear();
    }

    public void add(Denomination denomination, int count) {
        int currentCount = getCountForDenomination(denomination);
        data.put(denomination, currentCount + count);
    }

    public Iterable<Denomination> getContainedDenominations() {
        return data.keySet();
    }

    public int getCountForDenomination(Denomination denomination) {
        Integer count = data.get(denomination);
        return Objects.isNull(count) ? 0 : count;
    }

    public void setCountForDenomination(Denomination denomination, int count) {
        data.put(denomination, count);
    }

    public int getBanknotesTotalCount() {
        return data.values().stream().reduce(0, Integer::sum);
    }

    @Override
    public String toString() {
        return data.keySet().stream()
                .map(denomination ->  denomination.getValue() + " x " + data.get(denomination))
                .collect(Collectors.joining("\n"));
    }

}
