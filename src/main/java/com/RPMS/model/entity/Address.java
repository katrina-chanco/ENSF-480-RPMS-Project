package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int houseNo;
    private String  strName;
    private String city;
    private String postalCode;
    private String country;

    public Address(int houseNo, String strName, String city, String postalCode, String country) {
        this.strName = strName;
        this.houseNo = houseNo;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public Address() {

    }


    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public int getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(int houseNo) {
        this.houseNo = houseNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
