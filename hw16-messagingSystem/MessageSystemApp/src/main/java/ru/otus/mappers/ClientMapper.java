package ru.otus.mappers;

import org.mapstruct.Mapper;
import ru.otus.db.model.ClientDataSet;
import ru.otus.dto.ClientDto;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDataSet toModel(ClientDto clientDto);

    ClientDto toDto(ClientDataSet client);

}
