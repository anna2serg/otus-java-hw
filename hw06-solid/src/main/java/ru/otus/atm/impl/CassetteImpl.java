package ru.otus.atm.impl;

import ru.otus.banknote.Denomination;
import ru.otus.atm.Cassette;
import ru.otus.banknote.exception.UnavailableCountOfBanknotesException;

import java.util.Objects;

public class CassetteImpl implements Cassette {

    private final int BANKNOTES_CAPACITY = 2500;
    private Denomination banknoteDenomination;
    private int banknoteCount;

    public CassetteImpl() {
    }

    public CassetteImpl(Denomination banknoteDenomination, int banknoteCount) {
        pushBanknotes(banknoteDenomination, banknoteCount);
    }

    @Override
    public Denomination getBanknoteDenomination() {
        return banknoteDenomination;
    }

    @Override
    public int getBanknoteCount() {
        return banknoteCount;
    }

    @Override
    public double getBalance() {
        return Objects.nonNull(banknoteDenomination) ? banknoteDenomination.getValue() * banknoteCount : 0;
    }

    @Override
    public void reset() {
        banknoteDenomination = null;
        banknoteCount = 0;
    }

    @Override
    public void pushBanknotes(Denomination denomination, int count) {
        if ((this.banknoteCount + count) > BANKNOTES_CAPACITY) {
            throw new UnsupportedOperationException("The allowed number of banknotes in the cell has been exceeded");
        }
        if ((Objects.nonNull(this.banknoteDenomination))
                && (!Objects.equals(this.banknoteDenomination, denomination))
                && (this.banknoteCount > 0)
        ) {
            throw new UnsupportedOperationException(String.format("The cassette contains banknotes of a other denomination (%s)", banknoteDenomination));
        }
        this.banknoteCount = this.banknoteCount + count;
        this.banknoteDenomination = denomination;
    }

    @Override
    public int pullBanknotes(int count) {
        if (count > this.banknoteCount) {
            throw new UnavailableCountOfBanknotesException(count, this.banknoteCount);
        }
        this.banknoteCount = this.banknoteCount - count;
        return count;
    }

}
