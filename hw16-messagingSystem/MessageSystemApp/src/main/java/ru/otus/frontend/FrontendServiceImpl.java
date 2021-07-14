package ru.otus.frontend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.dto.ClientDto;
import ru.otus.dto.ClientListDto;
import ru.otus.dto.MessageDto;
import ru.otus.dto.MessageStatus;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

import static ru.otus.services.DatabaseMsClientService.DATABASE_SERVICE_CLIENT_NAME;

@Service
public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;

    public FrontendServiceImpl(@Qualifier("frontendMsClientService") MsClient msClient) {
        this.msClient = msClient;
    }

    @Override
    public void getClients(MessageCallback<MessageDto<ClientListDto>> callback) {
        final Message message = msClient.produceMessage(DATABASE_SERVICE_CLIENT_NAME, null, MessageType.CLIENT_LIST, callback);
        msClient.sendMessage(message);
    }

    @Override
    public void getClient(Long id, MessageCallback<MessageDto<ClientDto>> callback) {
        final MessageDto<ClientDto> clientMessage = new MessageDto<>(MessageStatus.SUCCESS, new ClientDto(id), null);
        final Message message = msClient.produceMessage(DATABASE_SERVICE_CLIENT_NAME, clientMessage, MessageType.CLIENT_DATA, callback);
        msClient.sendMessage(message);
    }

    @Override
    public void addClient(ClientDto client, MessageCallback<ClientDto> callback) {
        final Message message = msClient.produceMessage(DATABASE_SERVICE_CLIENT_NAME, client, MessageType.ADD_CLINT, callback);
        msClient.sendMessage(message);
    }

}
