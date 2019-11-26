package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table

public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String attribute;

    public Amenity(String attribute) {
        this.attribute = attribute;
    }

    public Amenity(){

    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
