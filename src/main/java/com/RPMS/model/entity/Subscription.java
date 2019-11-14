package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String query;

    @OneToOne(cascade = {CascadeType.ALL})
    private Registered_Renter subscriber;

    public Subscription(String query, Registered_Renter subscriber) {
        this.query = query;
        this.subscriber = subscriber;
    }

    public Subscription(){

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Registered_Renter getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Registered_Renter subscriber) {
        this.subscriber = subscriber;
    }
}
