package com.RPMS.model.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table
@NamedQuery(name = "Subscription.getAll", query = "SELECT s FROM Subscription s")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = {CascadeType.ALL})
    private Registered_Renter subscriber;

    @ElementCollection
    private Map<String, Object> amenitiesMap = new HashMap<String, Object>();

    public Subscription(Registered_Renter subscriber) {
        this.subscriber = subscriber;
    }

    public Subscription(){

    }

    public Map<String, Object> getMap() {
        return amenitiesMap;
    }

    public void setMap(Map<String, Object> amenitiesMap) {
        this.amenitiesMap = amenitiesMap;
    }

    public Registered_Renter getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Registered_Renter subscriber) {
        this.subscriber = subscriber;
    }
}
