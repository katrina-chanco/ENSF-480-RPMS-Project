package com.RPMS.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String terms;
    private String landlordSignatureBase64;
    private String renterSignatureBase64;
    private Date finalDateSigned;
    private Date expiryDate;

    public Contract(String terms, String landlordSignatureBase64, String renterSignatureBase64, Date finalDateSigned, Date expiryDate) {
        this.terms = terms;
        this.landlordSignatureBase64 = landlordSignatureBase64;
        this.renterSignatureBase64 = renterSignatureBase64;
        this.finalDateSigned = finalDateSigned;
        this.expiryDate = expiryDate;
    }

    public Contract(){

    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getLandlordSignatureBase64() {
        return landlordSignatureBase64;
    }

    public void setLandlordSignatureBase64(String landlordSignatureBase64) {
        this.landlordSignatureBase64 = landlordSignatureBase64;
    }

    public String getRenterSignatureBase64() {
        return renterSignatureBase64;
    }

    public void setRenterSignatureBase64(String renterSignatureBase64) {
        this.renterSignatureBase64 = renterSignatureBase64;
    }

    public Date getFinalDateSigned() {
        return finalDateSigned;
    }

    public void setFinalDateSigned(Date finalDateSigned) {
        this.finalDateSigned = finalDateSigned;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isSigned() {
        return landlordSignatureBase64 != null && renterSignatureBase64 != null;
    }
}

