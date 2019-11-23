package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
@NamedQuery(name = "Email.findEmail", query = "SELECT a FROM Email a WHERE a.emailAddress = :emailAddress")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String emailAddress;

    public Email(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Email() {
    }

    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
