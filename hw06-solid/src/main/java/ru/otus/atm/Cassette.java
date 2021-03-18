package ru.otus.atm;

import ru.otus.banknote.Denomination;

public interface Cassette {

    Denomination getBanknoteDenomination();
    int getBanknoteCount();
    double getBalance();

    void reset();
    void pushBanknotes(Denomination denomination, int count);
    int pullBanknotes(int count);

}
