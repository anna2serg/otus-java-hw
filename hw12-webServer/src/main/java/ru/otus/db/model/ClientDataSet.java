package ru.otus.db.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "client")
public class ClientDataSet implements Cloneable {

    @Id
    @Expose
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Expose
    @Size(min = 1)
    @Column(name = "name", nullable = false)
    private String name;

    @Expose
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @JoinColumn(
            name="address_id",
            referencedColumnName="id",
            foreignKey=@ForeignKey(name = "fk_address")
    )
    private AddressDataSet address;

    @Expose
    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<PhoneDataSet> phones;

    public ClientDataSet() {
    }

    public ClientDataSet(Long id, String name, AddressDataSet address, Set<PhoneDataSet> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = new HashSet<>();
        if (Objects.nonNull(phones)) {
            for (PhoneDataSet phone : phones) {
                this.addPhone(phone);
            }
        }
    }

    public ClientDataSet(String name) {
        this(null, name, null, null);
    }

    public ClientDataSet(Long id, String name) {
        this(id, name, null, null);
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
        if (Objects.isNull(address)) {
            if (this.address != null) {
                this.address.setClient(null);
            }
        } else {
            address.setClient(this);
        }
        this.address = address;
    }

    public Set<PhoneDataSet> getPhones() {
        return phones;
    }

    public void addPhone(PhoneDataSet phone){
        this.phones.add(phone);
        phone.setClient(this);
    }

    public void removePhone(PhoneDataSet phone){
        this.phones.remove(phone);
        phone.setClient(null);
    }

    @Override
    public ClientDataSet clone() {
        var client = new ClientDataSet(this.id, this.name);
        if (Objects.nonNull(this.address)) {
            var clonedAddress = this.address.clone();
            client.setAddress(clonedAddress);
        }
        for (PhoneDataSet phone : this.phones) {
            var clonedPhone = phone.clone();
            client.addPhone(clonedPhone);
        }
        return client;
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