package com.RPMS.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int cardNumber;
    private double amount;
    private Date transactionDate;

    public Payment(int cardNumber, double amount, Date transactionDate) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public Payment(){

    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
