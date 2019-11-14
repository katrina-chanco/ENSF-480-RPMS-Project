package com.RPMS.model.entity;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Manager extends Account {

    private String testManagerValue;

    public Manager(Address address, Name name, Email email, String testManagerValue) {
        super(address, name, email);
        this.testManagerValue = testManagerValue;
    }

    public Manager() {
        super();
    }

    public String getTestManagerValue() {
        return testManagerValue;
    }

    public void setTestManagerValue(String testManagerValue) {
        this.testManagerValue = testManagerValue;
    }
}
