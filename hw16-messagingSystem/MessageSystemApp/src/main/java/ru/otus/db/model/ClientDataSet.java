package ru.otus.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.*;

@Table("client")
public class ClientDataSet {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @MappedCollection(idColumn = "client_id", keyColumn = "client_id")
    private AddressDataSet address;

    @MappedCollection(idColumn = "client_id", keyColumn = "client_id")
    private Set<PhoneDataSet> phones;

    public ClientDataSet() {
        this(null, null, null, null);
    }

    @PersistenceConstructor
    public ClientDataSet(Long id, String name, AddressDataSet address, Set<PhoneDataSet> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }
    public ClientDataSet(String name, AddressDataSet address, Set<PhoneDataSet> phones) {
        this(null, name, address, phones);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public Set<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(Set<PhoneDataSet> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }

}