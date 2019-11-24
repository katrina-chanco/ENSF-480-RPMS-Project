package com.RPMS.controller.contact_strategy;

import com.RPMS.model.entity.Property;

public class ContactController {
    /**
     * Strategy for contacting landlord from renter
     */
    private ContactStrategy contactStrategy;
    /**
     * Singleton instance of ContactController
     */
    private static ContactController obj;

    /**
     * ContactController constructor
     */
    private ContactController() {
    }

    /**
     * getInstance method for the Singleton pattern
     *
     * @return instance of EmailController
     */
    public static ContactController getInstance() {
        if (obj == null) {
            obj = new ContactController();
        }
        return obj;
    }

    /**
     * Perform contact strategy
     * @param message
     */
    public void performContact(String message, String recepient, String subject) {
        contactStrategy.performContact(message, recepient, subject);
    }

    /**
     * Contact landlord from current account
     * @param message
     * @param property
     */
    public void performContactLandlord(String emailAddress, String message, Property property) {
        contactStrategy.contactLandlord(emailAddress, message, property);
    }

    /**
     * Set the method of contact
     *
     * @param other
     */
    public void setContactStrategy(ContactStrategy other) {
        this.contactStrategy = other;
    }
}
