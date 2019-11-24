package com.RPMS.controller;

import com.RPMS.controller.contact_strategy.ContactController;
import com.RPMS.controller.contact_strategy.EmailStrategy;
import com.RPMS.model.entity.Property;
import com.RPMS.model.entity.Subscription;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

public class SubscriptionController {
    /**
     * Allows querying in JPA
     */
    @PersistenceContext
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RPMS_PU");
    private EntityManager em;

    private static SubscriptionController obj;
    private static List<Subscription> subscriptionObservers;

    /**
     * Default constructor
     */
    private SubscriptionController() {
        subscriptionObservers = new LinkedList<>();
        getAllSubscriptions();
    }

    /**
     * getInstance method for the Singleton pattern
     *
     * @return instance of SubscriptionController
     */
    public static SubscriptionController getInstance() {
        if (obj == null) {
            obj = new SubscriptionController();
        }
        return obj;
    }

    private void unregisterSubscription(Subscription subscription) {
        subscriptionObservers.remove(subscription);
        em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.remove(subscription);
        em.getTransaction().commit();
        em.close();
    }

    private void getAllSubscriptions() {
        em = entityManagerFactory.createEntityManager();
        TypedQuery<Subscription> query = em.createNamedQuery("Subscription.getAll", Subscription.class);
        try {
            subscriptionObservers = query.getResultList();
        } catch (NullPointerException | NoResultException e) {
            e.printStackTrace();
        }
        em.close();
    }

    public void registerSubscription(Subscription subscription) {
        subscriptionObservers.add(subscription);
    }

    public void notifySubscribers(Property property) {
        ContactController.getInstance().setContactStrategy(new EmailStrategy());

        for (Subscription s : subscriptionObservers) {

            ContactController.getInstance().performContact("A property at " + property.getAddress().toString() + " has been added to our system. It matches your subscription criteria." +
                    "\n\nTo unsubscribe from this mailing list, please visit our website.", s.getSubscriber().getEmail().getEmailAddress(),
                    "RPMS SUBSCRIPTION: A property matching your subscription has been added!");
        }
        //if query matches subscribers, send email
        //TODO threaded

    }


}
