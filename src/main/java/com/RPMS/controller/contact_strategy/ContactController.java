package com.RPMS.controller.contact_strategy;

import com.RPMS.controller.LoginController;
import com.RPMS.model.entity.Property;
import com.RPMS.model.entity.Subscription;

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
    public void performContact(String emailAddress, String message, String recepient, String subject) {
        contactStrategy.performContact(emailAddress, message, recepient, subject);
    }

    /**
     * Contact landlord from current account
     * @param message
     * @param property
     */
    public void performContactLandlord(String emailAddress, String message, Property property) {
        setContactStrategy(new EmailStrategy());
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

    public void sendSubscriptionEmail(Subscription s, Property property) {
        performContact(LoginController.getInstance().getUserEmail(), "A property at "
                        + property.getAddress().toString() + " has been added to our system. It matches your subscription criteria." +
                        "\n\nTo unsubscribe from this mailing list, please visit our website.", s.getSubscriber().getEmail().getEmailAddress(),
                "RPMS SUBSCRIPTION: A property matching your subscription has been added!");
    }
}
