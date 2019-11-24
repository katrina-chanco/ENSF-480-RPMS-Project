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
    private String province;
    private String country;

    public Address(int houseNo, String strName, String city, String postalCode, String province, String country) {
        this.strName = strName;
        this.houseNo = houseNo;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.province = province;
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


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return houseNo + " " + strName + ", " + city + " " + province + " " + postalCode + " " + country;
    }

    public String toStringLines() {
        return houseNo + " " + strName + ", \n" + city + " \n" + province + " " + postalCode + " " + country;
    }
}
