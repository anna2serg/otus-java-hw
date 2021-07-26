package ru.otus.protobuf;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.*;
import ru.otus.protobuf.service.NumberStreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class GRPCClient {

    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private static final int INITIAL_NUM = 0;
    private static final int FINAL_NUM = 30;

    private static final long OUTPUT_DELAY_SEC = 1;

    private static int num;

    public static void main(String[] args) throws InterruptedException {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        CountDownLatch latch = new CountDownLatch(1);
        RemoteNumberGeneratorGrpc.RemoteNumberGeneratorStub stub = RemoteNumberGeneratorGrpc.newStub(channel);
        GeneratorOptionsMessage generatorOptionsMessage = GeneratorOptionsMessage.newBuilder()
                .setInitialNum(INITIAL_NUM)
                .setFinalNum(FINAL_NUM)
                .build();
        NumberStreamObserver numberStreamObserver = new NumberStreamObserver();
        stub.generate(generatorOptionsMessage, numberStreamObserver);

        IntStream.range(0, 50).forEach(indx ->
                {
                    num = num + numberStreamObserver.getValue() + 1;
                    logger.info("Current number: {}", num);
                    sleep();
                }
        );

        latch.await();
        channel.shutdown();
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(OUTPUT_DELAY_SEC));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
