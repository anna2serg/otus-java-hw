package ru.otus.frontend;

import ru.otus.dto.ClientDto;
import ru.otus.dto.ClientListDto;
import ru.otus.dto.MessageDto;
import ru.otus.messagesystem.client.MessageCallback;

public interface FrontendService {

    void getClients(MessageCallback<MessageDto<ClientListDto>> callback);

    void getClient(Long id, MessageCallback<MessageDto<ClientDto>> callback);

    void addClient(ClientDto client, MessageCallback<ClientDto> callback);

}
