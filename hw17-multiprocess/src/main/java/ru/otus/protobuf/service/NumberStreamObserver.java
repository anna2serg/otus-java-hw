package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.GeneratedNumberMessage;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NumberStreamObserver implements StreamObserver<GeneratedNumberMessage> {

    private static final Logger logger = LoggerFactory.getLogger(NumberStreamObserver.class);

    private Integer value = 0;
    private final Lock lock = new ReentrantLock();

    public int getValue() {
        lock.lock();
        try {
            int result = this.value;
            this.value = 0;
            return result;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onNext(GeneratedNumberMessage value) {
        lock.lock();
        try {
            this.value = value.getNum();
            logger.info("Generated number: {}", this.value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onError(Throwable t) {
        logger.error("An error occurred while streaming numbers", t);
    }

    @Override
    public void onCompleted() {
        logger.info("Numbers streaming is completed");
    }

}
