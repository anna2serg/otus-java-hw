package ru.otus.handlers;

import org.springframework.stereotype.Component;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.service.ClientService;
import ru.otus.dto.ClientDto;
import ru.otus.dto.ClientListDto;
import ru.otus.dto.MessageDto;
import ru.otus.dto.MessageStatus;
import ru.otus.mappers.ClientMapper;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("getClientListRequestHandler")
public class GetClientListRequestHandler implements RequestHandler<ClientListDto> {

    private final ClientService clientService;

    private final ClientMapper clientMapper;

    public GetClientListRequestHandler(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        MessageDto<ClientListDto> resultMessage = new MessageDto<>(MessageStatus.FAILURE, null, "Нет клиентов");
        List<ClientDataSet> clients = clientService.findAll();
        List<ClientDto> clientDtos = clients.stream().map(clientMapper::toDto).collect(Collectors.toList());
        if (clientDtos.size() > 0) {
            ClientListDto clientListDto = new ClientListDto(clientDtos);
            resultMessage = new MessageDto<>(MessageStatus.SUCCESS, clientListDto, null);
        }
        return Optional.of(MessageBuilder.buildReplyMessage(msg, MessageType.CLIENT_LIST, resultMessage));
    }
}
