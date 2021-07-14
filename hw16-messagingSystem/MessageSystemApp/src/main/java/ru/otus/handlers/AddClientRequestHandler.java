package ru.otus.handlers;

import org.springframework.stereotype.Component;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.service.ClientService;
import ru.otus.dto.ClientDto;
import ru.otus.mappers.ClientMapper;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.MessageType;

import java.util.Optional;

@Component("addClientRequestHandler")
public class AddClientRequestHandler implements RequestHandler<ClientDto> {

    private final ClientService clientService;

    private final ClientMapper clientMapper;

    public AddClientRequestHandler(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        ClientDto clientDto = MessageHelper.getPayload(msg);
        ClientDataSet addedClient = clientService.save(clientDto.toModel());
        ClientDto addedClientDto = clientMapper.toDto(addedClient);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, MessageType.ADD_CLINT, addedClientDto));
    }
}
