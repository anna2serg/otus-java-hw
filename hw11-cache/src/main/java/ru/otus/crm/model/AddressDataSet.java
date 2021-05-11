package ru.otus.crm.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "address")
public class AddressDataSet implements Cloneable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "street")
    private String street;

    @OneToOne(mappedBy = "address", fetch = FetchType.EAGER)
    private ClientDataSet client;

    public AddressDataSet() {
    }

    public AddressDataSet(UUID id, String street) {
        this.id = id;
        this.street = street;
    }

    public AddressDataSet(String street) {
        this(null, street);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public ClientDataSet getClient() {
        return client;
    }

    public void setClient(ClientDataSet client) {
        this.client = client;
    }

    @Override
    public AddressDataSet clone() {
        return new AddressDataSet(this.id, this.street);
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}