package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@NamedQuery(name="Registered_Renter.findRenter", query="SELECT r FROM Registered_Renter r WHERE r.email = :email")
@Table
public class Registered_Renter extends Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = {CascadeType.ALL})
    private Subscription subscription;

    public Registered_Renter(Address address, Name name, Email email, String password, Subscription subscription) {
        super(address, name, email, password);
        this.subscription = subscription;
    }

    public Registered_Renter(){
        super();
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
