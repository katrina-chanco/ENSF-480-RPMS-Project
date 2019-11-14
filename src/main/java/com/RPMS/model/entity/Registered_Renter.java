package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
public class Registered_Renter extends Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = {CascadeType.ALL})
    private Subscription subscription;

    public Registered_Renter(Address address, Name name, Email email, Subscription subscription) {
        super(address, name, email);
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
