package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean isSignedByLandlord;
    private boolean isSignedByRenter;

    public Contract() {
        this.isSignedByLandlord = false;
        this.isSignedByRenter = false;
    }

    public boolean isSigned() {
        return isSignedByRenter && isSignedByLandlord;
    }

    public boolean isSignedByRenter() {
        return isSignedByRenter;
    }

    public void setSignedByRenter(boolean signedByRenter) {
        isSignedByRenter = signedByRenter;
    }

    public boolean isSignedByLandlord() {
        return isSignedByLandlord;
    }

    public void setSignedByLandlord(boolean signedByLandlord) {
        isSignedByLandlord = signedByLandlord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

