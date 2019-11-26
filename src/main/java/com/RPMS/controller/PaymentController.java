package com.RPMS.controller;

import com.RPMS.model.entity.Landlord;
import com.RPMS.model.entity.Payment;
import com.RPMS.model.entity.Property;
import elemental.json.JsonObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

public class PaymentController {
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RPMS_PU");
    private EntityManager entityManager;

    /**
     * Singleton pattern
     */
    private static PaymentController instance;

    private PaymentController() {
    }

    public static PaymentController getInstance() {
        if (instance == null) {
            instance = new PaymentController();
        }
        return instance;
    }

    public boolean isPaymentNeeded() {
        return  LoginController.getInstance().getAccountType().equals("Landlord");
    }

    /**
     * Save/update property in DB
     * @param property property object
     */
    public void addPayment(Property property, JsonObject eventData, double cost) {

//        NOTE THIS WOULD HAVE TO USE API (see stripe) TO CHARGE USER
//        BLACK HOLE PAYMENT FOR DEMO!!!!
        String cardNumber = eventData.getObject("details").getString("cardNumber");
        int cardEnding = Integer.parseInt(cardNumber.substring(cardNumber.length() - 4));
        Payment payment = new Payment(cardEnding, cost, new Date());
        property.getLandlord().getPayments().add(payment);
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(property);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
