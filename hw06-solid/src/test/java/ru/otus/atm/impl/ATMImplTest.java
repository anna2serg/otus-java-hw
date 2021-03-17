package ru.otus.atm.impl;

import org.junit.jupiter.api.*;
import ru.otus.atm.ATM;
import ru.otus.atm.Cassette;
import ru.otus.banknote.Combination;
import ru.otus.banknote.exception.CombinationNotFoundException;

import java.util.Comparator;

import static ru.otus.banknote.Denomination.*;
import static ru.otus.banknote.Denomination.ONE_HUNDRED;

class ATMImplTest {

    private ATM atm;
    private final int FIVE_HUNDRED_BANKNOTES_INITIAL_COUNT = 8;
    private final int TWO_HUNDRED_BANKNOTES_INITIAL_COUNT = 2;
    private final int ONE_HUNDRED_BANKNOTES_INITIAL_COUNT = 10;
    private final int ONE_THOUSAND_BANKNOTES_ADDED_COUNT = 4;
    private final double AMOUNT4900 = 4900.00;

    @BeforeEach
    void setUp() {
        atm = new ATMImpl();
        atm.setCombinationStrategy(Comparator.comparingInt(Combination::getBanknotesTotalCount));
        Cassette cassette500 = new CassetteImpl(FIVE_HUNDRED, FIVE_HUNDRED_BANKNOTES_INITIAL_COUNT);
        atm.addCassette(cassette500);
        Cassette cassette200 = new CassetteImpl(TWO_HUNDRED, TWO_HUNDRED_BANKNOTES_INITIAL_COUNT);
        atm.addCassette(cassette200);
        Cassette cassette100 = new CassetteImpl(ONE_HUNDRED, ONE_HUNDRED_BANKNOTES_INITIAL_COUNT);
        atm.addCassette(cassette100);
    }

    @Test
    @DisplayName("Проверка стартового баланса банкомата")
    void checkInitialCashBalance() {
        double expectedCashBalance = FIVE_HUNDRED.getValue() * FIVE_HUNDRED_BANKNOTES_INITIAL_COUNT
                + TWO_HUNDRED.getValue() * TWO_HUNDRED_BANKNOTES_INITIAL_COUNT
                + ONE_HUNDRED.getValue() * ONE_HUNDRED_BANKNOTES_INITIAL_COUNT;
        double actualCashBalance = atm.getCashBalance();
        Assertions.assertEquals(expectedCashBalance, actualCashBalance);
    }

    @Test
    @DisplayName("Проверка баланса банкомата после вставки новой денежной кассеты")
    void checkCashBalanceAfterCassetteAdding() {
        Cassette cassette1000 = new CassetteImpl(ONE_THOUSAND, ONE_THOUSAND_BANKNOTES_ADDED_COUNT);
        atm.addCassette(cassette1000);
        double expectedCashBalance = ONE_THOUSAND.getValue() * ONE_THOUSAND_BANKNOTES_ADDED_COUNT
                + FIVE_HUNDRED.getValue() * FIVE_HUNDRED_BANKNOTES_INITIAL_COUNT
                + TWO_HUNDRED.getValue() * TWO_HUNDRED_BANKNOTES_INITIAL_COUNT
                + ONE_HUNDRED.getValue() * ONE_HUNDRED_BANKNOTES_INITIAL_COUNT;
        double actualCashBalance = atm.getCashBalance();
        Assertions.assertEquals(expectedCashBalance, actualCashBalance);
    }

    @Test
    @DisplayName("Проброс исключения в случае недостатка наличных для выдачи запрошенной суммы")
    void throwingExceptionWhenThereIsNotEnoughCash() {
        Assertions.assertThrows(CombinationNotFoundException.class, () -> atm.dispenseCash(AMOUNT4900));
    }

    @Test
    @DisplayName("Проверка выданной комбинации купюр при наличии достаточной суммы наличных и максимального доступного номинала в 500")
    void checkCombinationWhenThereAreEnough500() {
        Cassette cassette500 = new CassetteImpl(FIVE_HUNDRED, 1);
        atm.addCassette(cassette500);

        Combination actualCombination = atm.dispenseCash(AMOUNT4900);

        Combination expectedCombination = new Combination();
        expectedCombination.add(FIVE_HUNDRED, 9);
        expectedCombination.add(TWO_HUNDRED, 2);

        Assertions.assertEquals(expectedCombination, actualCombination);
        Assertions.assertEquals(expectedCombination.getBanknotesTotalCount(), actualCombination.getBanknotesTotalCount());
    }

    @Test
    @DisplayName("Проверка выданной комбинации купюр при наличии достаточной суммы наличных и максимального доступного номинала в 1000")
    void checkCombinationWhenThereAreEnough1000() {
        Cassette cassette1000 = new CassetteImpl(ONE_THOUSAND, ONE_THOUSAND_BANKNOTES_ADDED_COUNT);
        atm.addCassette(cassette1000);

        Combination actualCombination = atm.dispenseCash(AMOUNT4900);

        Combination expectedCombination = new Combination();
        expectedCombination.add(ONE_THOUSAND, 4);
        expectedCombination.add(FIVE_HUNDRED, 1);
        expectedCombination.add(TWO_HUNDRED, 2);

        Assertions.assertEquals(expectedCombination, actualCombination);
        Assertions.assertEquals(expectedCombination.getBanknotesTotalCount(), actualCombination.getBanknotesTotalCount());
    }

    @Test
    @DisplayName("Проверка извлечения купюр одного номинала из разных кассет при наличии достаточной суммы наличных в них")
    void checkCombinationWhenThereAreEnough1000InDifferentCassettes() {
        Cassette cassette1000_1 = new CassetteImpl(ONE_THOUSAND, 2);
        atm.addCassette(cassette1000_1);
        Cassette cassette1000_2 = new CassetteImpl(ONE_THOUSAND, 1);
        atm.addCassette(cassette1000_2);
        Cassette cassette1000_3 = new CassetteImpl(ONE_THOUSAND, 1);
        atm.addCassette(cassette1000_3);

        Combination actualCombination = atm.dispenseCash(AMOUNT4900);

        Combination expectedCombination = new Combination();
        expectedCombination.add(ONE_THOUSAND, 4);
        expectedCombination.add(FIVE_HUNDRED, 1);
        expectedCombination.add(TWO_HUNDRED, 2);

        Assertions.assertEquals(expectedCombination, actualCombination);
        Assertions.assertEquals(expectedCombination.getBanknotesTotalCount(), actualCombination.getBanknotesTotalCount());
    }

    @AfterEach
    void tearDown() {
        atm.removeAllCassette();
    }
}