package ru.otus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequencePrinter {

    private static final Logger logger = LoggerFactory.getLogger(SequencePrinter.class);
    private static final String THREAD_NAME_TEMPLATE = "Поток";

    private final CustomSequence sequence;
    private String expectedThreadName = "";
    private boolean isSequencePassed = false;

    public SequencePrinter(CustomSequence sequence) {
        this.sequence = sequence;
    }

    public void print(int threadsCount) {
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        for (var idx = 1; idx <= threadsCount; idx++) {
            var threadName = generateThreadNameByNum(idx);
            if (idx == 1) {
                expectedThreadName = threadName;
            }
            executor.execute(() -> this.action(threadName, threadsCount));
        }
        executor.shutdown();
    }

    public synchronized void action(String threadName, int threadsCount) {
        while (!isSequencePassed) try {

            if (!threadName.equals(expectedThreadName)) {
                this.wait();
            }

            var curNum = sequence.next();
            logger.info("{}: {} ", threadName, curNum);

            expectedThreadName = determineExpectedThreadNameByCurrent(threadName, threadsCount);

            sleep();
            notifyAll();

        } catch (InterruptedException e) {
            isSequencePassed = true;
            Thread.currentThread().interrupt();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private String generateThreadNameByNum(int threadNum) {
        return THREAD_NAME_TEMPLATE + " " + threadNum;
    }

    private int extractThreadNumByName(String threadName) {
        var threadNum = threadName.substring(threadName.indexOf(THREAD_NAME_TEMPLATE) +
                THREAD_NAME_TEMPLATE.length());
        return Integer.parseInt(threadNum.trim());
    }

    private String determineExpectedThreadNameByCurrent(String threadName, int threadCount) {
        int currentThreadNum = extractThreadNumByName(threadName);
        int expectedThreadNum = currentThreadNum + 1;
        if (expectedThreadNum > threadCount) {
            expectedThreadNum = 1;
        }
        return generateThreadNameByNum(expectedThreadNum);
    }
}