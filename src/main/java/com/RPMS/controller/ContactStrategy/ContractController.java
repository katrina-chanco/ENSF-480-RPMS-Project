package com.RPMS.controller.ContactStrategy;

import com.RPMS.model.entity.Property;

public class ContractController {
    /**
     * Strategy for contacting landlord from renter
     */
    private ContactStrategy contactStrategy;
    /**
     * Singleton instance of ContactController
     */
    private static ContractController obj;

    /**
     * ContactController constructor
     */
    private ContractController() {
    }

    /**
     * getInstance method for the Singleton pattern
     *
     * @return instance of EmailController
     */
    public static ContractController getInstance() {
        if (obj == null) {
            obj = new ContractController();
        }
        return obj;
    }

    /**
     * Perform contact strategy
     * @param message
     * @param property
     */
    public void performContact(String message, Property property) {
        contactStrategy.contactLandlord(message, property);
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
