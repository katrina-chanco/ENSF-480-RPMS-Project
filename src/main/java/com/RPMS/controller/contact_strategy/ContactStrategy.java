package com.RPMS.controller.contact_strategy;

import com.RPMS.model.entity.Property;

/**
 * Strategy pattern for performing contact
 */
public interface ContactStrategy {
    /**
     * Contacts recipient
     *
     * @param message
     * @param recipient
     * @param subject
     */
    void performContact(String emailAddress, String message, String recipient, String subject);

    /**
     * Contacts landlord of selected property⁄
     *
     * @param message
     * @param property
     */
    void contactLandlord(String emailAddress, String message, Property property);
}
