package com.RPMS.controller;

public class SubscriptionController {

    private static SubscriptionController obj;

    /**
     * Default constructor
     */
    private SubscriptionController() {

    }

    /**
     * getInstance method for the Singleton pattern
     *
     * @return instance of LoginController
     */
    public static SubscriptionController getInstance() {
        if (obj == null) {
            obj = new SubscriptionController();
        }
        return obj;
    }
}
