package com.RPMS.model.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table
public class Property {

    public enum Pets_Allowed{
        DOGS_ALLOWED,
        CATS_ALLOWED,
        DOGS_AND_CATS_ALLOWED,
        NO_PETS_ALLOWED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double price;
    private int beds;
    private int bathrooms;

    private Date dateAdded;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Amenity> amenities = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Pets_Allowed petsAllowed;

    @OneToOne(cascade = {CascadeType.ALL})
    private Landlord landlord;

    @OneToOne(cascade = {CascadeType.ALL})
    private Contract contract;

    @OneToOne(cascade = {CascadeType.ALL})
    private Address address;

    public Property(Landlord landlord, Contract contract, Address address) {
        this.landlord = landlord;
        this.contract = contract;
        this.address = address;
    }

    public Property(){

    }

    public Landlord getLandlord() {
        return landlord;
    }

    public void setLandlord(Landlord landlord) {
        this.landlord = landlord;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
