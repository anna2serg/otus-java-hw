package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.GeneratedNumberMessage;
import ru.otus.protobuf.generated.GeneratorOptionsMessage;
import ru.otus.protobuf.generated.RemoteNumberGeneratorGrpc;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class RemoteNumberGeneratorImpl extends RemoteNumberGeneratorGrpc.RemoteNumberGeneratorImplBase {

    private static final Logger logger = LoggerFactory.getLogger(RemoteNumberGeneratorImpl.class);
    private static final long GENERATION_DELAY_SEC = 2;

    @Override
    public void generate(GeneratorOptionsMessage request, StreamObserver<GeneratedNumberMessage> responseObserver) {
        var initialNum = request.getInitialNum();
        var finalNum = request.getFinalNum();
        IntStream.range(initialNum, finalNum).forEach(num ->
                {
                    sleep();
                    logger.info("generated number: {}", num);
                    responseObserver.onNext(num2GeneratedNumberMessage(num));
                }
        );
        responseObserver.onCompleted();
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(GENERATION_DELAY_SEC));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private GeneratedNumberMessage num2GeneratedNumberMessage(Integer num) {
        return GeneratedNumberMessage.newBuilder()
                .setNum(num)
                .build();
    }

}
