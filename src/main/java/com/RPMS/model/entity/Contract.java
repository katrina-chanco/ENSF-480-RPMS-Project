package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int envelopeId;

    public Contract() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContractId() {
        return id + 1;
    }

    public int getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(int envelopeId) {
        this.envelopeId = envelopeId;
    }
}

