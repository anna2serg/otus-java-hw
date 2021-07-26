package ru.otus.dto;

import ru.otus.messagesystem.client.ResultDataType;

import java.util.List;

public class ClientListDto extends ResultDataType {

    private final List<ClientDto> data;

    public ClientListDto(List<ClientDto> clientDtoList) {
        this.data = clientDtoList;
    }

    public List<ClientDto> getData() {
        return data;
    }

}
