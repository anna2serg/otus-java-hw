package ru.otus.handlers;

import org.springframework.stereotype.Component;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.service.ClientService;
import ru.otus.dto.ClientDto;
import ru.otus.dto.MessageDto;
import ru.otus.dto.MessageStatus;
import ru.otus.mappers.ClientMapper;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.MessageType;

import java.util.Objects;
import java.util.Optional;

@Component("getClientDataRequestHandler")
public class GetClientDataRequestHandler implements RequestHandler<ClientDto> {

    private final ClientService clientService;

    private final ClientMapper clientMapper;

    public GetClientDataRequestHandler(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        MessageDto<ClientDto> resultMessage = new MessageDto<>(MessageStatus.FAILURE, null, "Клиент не найден");
        MessageDto<ClientDto> clientMessageDto = MessageHelper.getPayload(msg);
        ClientDto clientDto = clientMessageDto.getResult();
        if (Objects.nonNull(clientDto)) {
            Optional<ClientDataSet> client = clientService.get(clientDto.getId());
            clientDto = client.map(clientMapper::toDto).orElse(null);
            if (Objects.nonNull(clientDto)) {
                resultMessage = new MessageDto<>(MessageStatus.SUCCESS, clientDto, null);
            }
        }
        return Optional.of(MessageBuilder.buildReplyMessage(msg, MessageType.CLIENT_DATA, resultMessage));
    }
}
