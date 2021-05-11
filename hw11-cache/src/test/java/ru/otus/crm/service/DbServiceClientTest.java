package ru.otus.crm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.cachehw.HwCacheAction;
import ru.otus.cachehw.HwListener;
import ru.otus.crm.model.ClientDataSet;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@DisplayName("Тестирование кэша сервиса DbServiceClient")
class DbServiceClientTest extends AbstractHibernateTest {

    private final long TEST_CLIENT_FROM_DB_ID = 0L;

    @Test
    @DisplayName("получить клиента из кэша быстрее чем из БД")
    void cacheShouldBeFasterThanDB() {

        long start = System.nanoTime();
        var clientFromDB = dbServiceClient.getClient(TEST_CLIENT_FROM_DB_ID);
        long finish = System.nanoTime();

        assertThat(clientFromDB).isPresent();

        long clientFromDBTimeElapsed = finish - start;

        start = System.nanoTime();
        var clientFromCache = dbServiceClient.getClient(TEST_CLIENT_FROM_DB_ID);
        finish = System.nanoTime();

        assertThat(clientFromCache).isPresent();

        long clientFromCacheTimeElapsed = finish - start;

        org.hamcrest.MatcherAssert.assertThat("time to retrieve the client from the DB",
                clientFromDBTimeElapsed, greaterThan(clientFromCacheTimeElapsed));
    }

    /**
     * VM options: -Xmx16m -Xms16m -Xlog:gc=debug
     */
    @Test
    @DisplayName("кэш должен быть очищен при нехватке памяти")
    void cacheShouldBeFlushedWhenOutOfMemory() {

        final int[] addActionCount = {0};
        HwListener<String, ClientDataSet> testListener = new HwListener<String, ClientDataSet>() {
            @Override
            public void notify(String key, ClientDataSet value, HwCacheAction action) {
                if (action.equals(HwCacheAction.ADD)) {
                    addActionCount[0]++;
                }
            }
        };
        clientCache.addListener(testListener);

        int size = 1010;
        List<WeakReference<BigObject>> references = new ArrayList<>(size);

        for (int k = 0; k < size; k++) {
            references.add(new WeakReference<>(new BigObject()));
            var client = dbServiceClient.getClient(TEST_CLIENT_FROM_DB_ID);
            assertThat(client).isPresent();
        }

        org.hamcrest.MatcherAssert.assertThat("the number of events of adding a client to the cache",
                addActionCount[0], greaterThan(1));
    }

    static class BigObject {
        final byte[] array = new byte[1024 * 1024];
    }
}