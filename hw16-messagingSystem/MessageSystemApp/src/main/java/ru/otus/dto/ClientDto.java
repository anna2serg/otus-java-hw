package ru.otus.dto;

import org.apache.logging.log4j.util.Strings;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.model.PhoneDataSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDto {

    private String name;
    private AddressDto address;
    private List<PhoneDto> phones;

    public String getName() {
        return name;
    }

    public AddressDto getAddress() {
        return address;
    }

    public List<PhoneDto> getPhones() {
        return phones;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public void setPhones(List<PhoneDto> phones) {
        this.phones = phones;
    }

    public ClientDataSet toModel() {
        var addressDataSet = getAddress().toModel();
        Set<PhoneDataSet> phoneDataSets = getPhones().stream()
                .filter(phoneDto -> Strings.isNotBlank(phoneDto.getNumber()))
                .map(PhoneDto::toModel)
                .collect(Collectors.toSet());
        return new ClientDataSet(getName(), addressDataSet, phoneDataSets);
    }

}
