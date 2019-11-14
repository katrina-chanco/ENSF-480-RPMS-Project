package com.RPMS.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table
public class Landlord extends Account {

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Property> properties = new LinkedList<>();

    @OneToMany(cascade = {CascadeType.ALL})
    private  List<Payment> payments = new LinkedList<>();

    public Landlord(Address address, Name name, Email email, List<Property> properties, List<Payment> payments) {
        super(address, name, email);
        this.properties = properties;
        this.payments = payments;
    }

    public Landlord(){
        super();
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
