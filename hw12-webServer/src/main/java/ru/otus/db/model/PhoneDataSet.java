package ru.otus.db.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "phone")
public class PhoneDataSet implements Cloneable {

    @Id
    @Expose
    @SequenceGenerator(name="phone_generator", sequenceName="phone_seq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="phone_generator")
    @Column(name = "id")
    private Long id;

    @Expose
    @Size(min = 1)
    @Column(name = "number", nullable = false)
    private String number;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "client_id",
            nullable = false,
            referencedColumnName="id",
            foreignKey=@ForeignKey(name = "fk_client")
    )
    private ClientDataSet client;

    public PhoneDataSet() {
    }

    public PhoneDataSet(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public PhoneDataSet(String number) {
        this(null, number);
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ClientDataSet getClient() {
        return client;
    }

    public void setClient(ClientDataSet client) {
        this.client = client;
    }

    @Override
    public PhoneDataSet clone() {
        return new PhoneDataSet(this.id, this.number);
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }

}
