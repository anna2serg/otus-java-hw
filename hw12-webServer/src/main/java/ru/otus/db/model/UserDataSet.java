package ru.otus.db.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserDataSet {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Size(min = 1)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(min = 1)
    @Column(name = "login", unique=true, nullable = false)
    private String login;

    @Size(min = 1)
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<RoleDataSet> roles = new HashSet<>();

    public UserDataSet() {
    }

    public UserDataSet(long id, String name, String login, String password, Set<RoleDataSet> roles) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        if (Objects.nonNull(roles)) {
            this.roles.addAll(roles);
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Set<RoleDataSet> getRoles() {
        return roles;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", roles=" + roles +
                '}';
    }
}
