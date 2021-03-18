package ru.otus.banknote.impl;

import org.junit.jupiter.api.*;
import ru.otus.banknote.Combination;
import ru.otus.banknote.Combinator;

import java.util.Comparator;
import java.util.List;

import static ru.otus.banknote.Denomination.*;

class CombinatorImplTest {

    private Combinator combinator = new CombinatorImpl();
    private final double AMOUNT4900 = 4900.00;

    @BeforeEach
    void setUp() {
        combinator.setCombinationStrategy(Comparator.comparingInt(Combination::getBanknotesTotalCount));
    }

    @Test
    @DisplayName("Проверка получения всех из возможных комбинаций выдачи запрошенной суммы")
    void checkingForAllPossibleCombinationsAreReceived() {
        int expectedCombinationsCount = 12;
        List<Combination> actualCombinationList = combinator.getAllCombinations(AMOUNT4900);
        int actualCombinationsCount = actualCombinationList.size();
        Assertions.assertEquals(expectedCombinationsCount, actualCombinationsCount);

        Combination combination1 = new Combination();
        combination1.add(ONE_HUNDRED, 49);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination1)));

        Combination combination2 = new Combination();
        combination2.add(TWO_HUNDRED, 24);
        combination2.add(ONE_HUNDRED, 1);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination2)));

        Combination combination3 = new Combination();
        combination3.add(FIVE_HUNDRED, 9);
        combination3.add(ONE_HUNDRED, 4);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination3)));

        Combination combination4 = new Combination();
        combination4.add(FIVE_HUNDRED, 9);
        combination4.add(TWO_HUNDRED, 2);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination4)));

        Combination combination5 = new Combination();
        combination5.add(ONE_THOUSAND, 4);
        combination5.add(ONE_HUNDRED, 9);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination5)));

        Combination combination6 = new Combination();
        combination6.add(ONE_THOUSAND, 4);
        combination6.add(TWO_HUNDRED, 4);
        combination6.add(ONE_HUNDRED, 1);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination6)));

        Combination combination7 = new Combination();
        combination7.add(ONE_THOUSAND, 4);
        combination7.add(FIVE_HUNDRED, 1);
        combination7.add(ONE_HUNDRED, 4);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination7)));

        Combination combination8 = new Combination();
        combination8.add(ONE_THOUSAND, 4);
        combination8.add(FIVE_HUNDRED, 1);
        combination8.add(TWO_HUNDRED, 2);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination8)));

        Combination combination9 = new Combination();
        combination9.add(TWO_THOUSAND, 2);
        combination9.add(ONE_HUNDRED, 9);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination9)));

        Combination combination10 = new Combination();
        combination10.add(TWO_THOUSAND, 2);
        combination10.add(TWO_HUNDRED, 4);
        combination10.add(ONE_HUNDRED, 1);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination10)));

        Combination combination11 = new Combination();
        combination11.add(TWO_THOUSAND, 2);
        combination11.add(FIVE_HUNDRED, 1);
        combination11.add(ONE_HUNDRED, 4);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination11)));

        Combination combination12 = new Combination();
        combination12.add(TWO_THOUSAND, 2);
        combination12.add(FIVE_HUNDRED, 1);
        combination12.add(TWO_HUNDRED, 2);
        Assertions.assertTrue(actualCombinationList.stream().anyMatch(e -> e.equals(combination12)));
    }

    @AfterEach
    void tearDown() {
        combinator.resetAvailableCombination();
    }
}