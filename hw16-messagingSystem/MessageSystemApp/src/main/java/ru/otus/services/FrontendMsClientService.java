package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.handlers.AddClientResponseHandler;
import ru.otus.handlers.GetClientDataResponseHandler;
import ru.otus.handlers.GetClientListResponseHandler;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.*;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

@Service
public class FrontendMsClientService implements MsClient {

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";

    private final MsClient frontendMsClient;

    public FrontendMsClientService(MessageSystem messageSystem, CallbackRegistry callbackRegistry,
                                   GetClientListResponseHandler getClientListResponseHandler,
                                   GetClientDataResponseHandler getClientDataResponseHandler,
                                   AddClientResponseHandler addClientResponseHandler) {
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.CLIENT_LIST, getClientListResponseHandler);
        requestHandlerFrontendStore.addHandler(MessageType.CLIENT_DATA, getClientDataResponseHandler);
        requestHandlerFrontendStore.addHandler(MessageType.ADD_CLINT, addClientResponseHandler);
        this.frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem, requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);
    }

    @Override
    public boolean sendMessage(Message msg) {
        return frontendMsClient.sendMessage(msg);
    }

    @Override
    public void handle(Message msg) {
        frontendMsClient.handle(msg);
    }

    @Override
    public String getName() {
        return frontendMsClient.getName();
    }

    @Override
    public <T extends ResultDataType> Message produceMessage(String to, T data, MessageType msgType, MessageCallback<T> callback) {
        return frontendMsClient.produceMessage(to, data, msgType, callback);
    }
}
