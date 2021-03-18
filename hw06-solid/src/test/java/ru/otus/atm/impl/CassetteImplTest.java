package ru.otus.atm.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.atm.Cassette;
import ru.otus.banknote.Denomination;
import ru.otus.banknote.exception.UnavailableCountOfBanknotesException;

import static ru.otus.banknote.Denomination.*;

class CassetteImplTest {

    private Cassette cassette = new CassetteImpl();
    private final int BANKNOTES_INITIAL_COUNT = 5;
    private final Denomination BANKNOTES_INITIAL_DENOMINATION = FIVE_THOUSAND;
    private final int ADDED_BANKNOTES_COUNT = 4;
    private final int EXTRACTED_BANKNOTES_COUNT = 2;
    private final int EXTRACTED_MORE_THAN_THERE_IS_BANKNOTES_COUNT = 6;
    private final int BANKNOTES_COUNT_TO_EXCEED_CAPACITY = 2496;

    @BeforeEach
    void setUp() {
        cassette.pushBanknotes(BANKNOTES_INITIAL_DENOMINATION, BANKNOTES_INITIAL_COUNT);
    }

    @Test
    @DisplayName("Проверка стартового баланса кассеты")
    void checkInitialBalance() {
        double expectedBalance = BANKNOTES_INITIAL_DENOMINATION.getValue() * BANKNOTES_INITIAL_COUNT;
        double actualBalance = cassette.getBalance();
        Assertions.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("Проверка баланса кассеты после пополнения")
    void checkBalanceAfterPushBanknotes() {
        cassette.pushBanknotes(BANKNOTES_INITIAL_DENOMINATION, ADDED_BANKNOTES_COUNT);
        double expectedBalance = BANKNOTES_INITIAL_DENOMINATION.getValue() * (BANKNOTES_INITIAL_COUNT + ADDED_BANKNOTES_COUNT);
        double actualBalance = cassette.getBalance();
        Assertions.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("Проброс исключения при попытке добавить в кассету купюры другого номинала")
    void throwingExceptionWhenAddingBanknotesOtherDenomination() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> cassette.pushBanknotes(TWO_THOUSAND, ADDED_BANKNOTES_COUNT),
                String.format("The cassette contains banknotes of a other denomination (%s)", BANKNOTES_INITIAL_DENOMINATION));
    }

    @Test
    @DisplayName("Проброс исключения при попытке добавить в кассету купюры в количестве, превышающем емкость")
    void throwingExceptionWhenAddingBanknotesCountOverCapacity() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> cassette.pushBanknotes(BANKNOTES_INITIAL_DENOMINATION, BANKNOTES_COUNT_TO_EXCEED_CAPACITY),
                "The allowed number of banknotes in the cell has been exceeded");
    }

    @Test
    @DisplayName("Проверка баланса кассеты после частичного извлечения купюр")
    void checkBalanceAfterPullBanknotes() {
        cassette.pullBanknotes(EXTRACTED_BANKNOTES_COUNT);
        double expectedBalance = BANKNOTES_INITIAL_DENOMINATION.getValue() * (BANKNOTES_INITIAL_COUNT - EXTRACTED_BANKNOTES_COUNT);
        double actualBalance = cassette.getBalance();
        Assertions.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("Проверка баланса кассеты после полного извлечения купюр")
    void checkBalanceAfterReset() {
        cassette.reset();
        double expectedBalance = 0;
        double actualBalance = cassette.getBalance();
        Assertions.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("Проброс исключения при попытке извлечь из кассеты больше купюр, чем она содержит")
    void throwingExceptionWhenPullBanknotesMoreThanThereIs() {
        Assertions.assertThrows(UnavailableCountOfBanknotesException.class,
                () -> cassette.pullBanknotes(EXTRACTED_MORE_THAN_THERE_IS_BANKNOTES_COUNT),
                String.format("The requested number of banknotes is not available: requested = %d, available = %d",
                        EXTRACTED_MORE_THAN_THERE_IS_BANKNOTES_COUNT, BANKNOTES_INITIAL_COUNT));
    }

}