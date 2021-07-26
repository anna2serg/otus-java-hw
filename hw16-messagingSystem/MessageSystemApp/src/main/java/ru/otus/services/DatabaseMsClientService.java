package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.handlers.AddClientRequestHandler;
import ru.otus.handlers.GetClientDataRequestHandler;
import ru.otus.handlers.GetClientListRequestHandler;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;

@Service
public class DatabaseMsClientService {

    public static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    private final MsClient databaseMsClient;

    public DatabaseMsClientService(MessageSystem messageSystem, CallbackRegistry callbackRegistry,
                                   GetClientListRequestHandler getClientListRequestHandler,
                                   GetClientDataRequestHandler getClientDataRequestHandler,
                                   AddClientRequestHandler addClientRequestHandler) {
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.CLIENT_LIST, getClientListRequestHandler);
        requestHandlerDatabaseStore.addHandler(MessageType.CLIENT_DATA, getClientDataRequestHandler);
        requestHandlerDatabaseStore.addHandler(MessageType.ADD_CLINT, addClientRequestHandler);
        this.databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(this.databaseMsClient);
    }
}
