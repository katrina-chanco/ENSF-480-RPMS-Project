package com.RPMS.model.entity;

import javax.persistence.*;



@Entity
@Table
@DiscriminatorColumn(name = "AccountType")
@NamedQueries({
        @NamedQuery(name="Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email.emailAddress = :email")
})
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

    public Account(Address address, Name name, Email email) {
        this.address = address;
        this.name = name;
        this.email = email;
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
}
