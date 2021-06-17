package ru.otus.db.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "roles")
public class RoleDataSet {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Size(min = 1)
    @Column(name = "name", nullable = false)
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "RoleDataSet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
