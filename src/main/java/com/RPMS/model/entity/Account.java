package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
        @NamedQuery(name = "Account.validateLogin", query = "SELECT a FROM Account a " +
                "WHERE a.email = :email" +
                " AND a.password = :password"),
})
@DiscriminatorColumn(name = "accountType")
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = {CascadeType.ALL})
    private Address address;

    @OneToOne(cascade = {CascadeType.ALL})
    private Name name;

    @OneToOne(cascade = {CascadeType.ALL})
    private Email email;

    private String password;

    public Account(Address address, Name name, Email email, String password) {
        this.address = address;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Account() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
