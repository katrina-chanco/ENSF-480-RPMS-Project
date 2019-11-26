package com.RPMS.controller;

import com.RPMS.controller.contact_strategy.ContactController;
import com.RPMS.controller.contact_strategy.EmailStrategy;
import com.RPMS.model.entity.Property;
import com.RPMS.model.entity.Subscription;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public void saveSubscription(Subscription subscription) {
        em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.merge(subscription);
        em.getTransaction().commit();
        em.close();
    }

    public void unregisterSubscription(Subscription subscription) {
        subscriptionObservers.remove(subscription);
        subscription.getSubscriber().setSubscription(null);
        LoginController.getInstance().mergeAccount(subscription.getSubscriber());
//        em = entityManagerFactory.createEntityManager();
//        em.getTransaction().begin();
//        em.remove(subscription);
//        em.getTransaction().commit();
//        em.close();

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
        subscription.getSubscriber().setSubscription(subscription);
//        saveSubscription(subscription);
        LoginController.getInstance().mergeAccount(subscription.getSubscriber());
    }

    public void notifySubscribers(Property property) {
        ContactController.getInstance().setContactStrategy(new EmailStrategy());
        Map<String, Object> stringObjectMap;
        List<Property> propertyList;
        for (Subscription s : subscriptionObservers) {
            stringObjectMap = s.convertMapObject(s.getMap());
            propertyList = PropertyController.getInstance().getAllProperties(stringObjectMap);
            propertyList.add(property);
            for (Property p : propertyList) {
                if (p.getAddress().toString().equals(property.getAddress().toString())) {
                    ContactController.getInstance().sendSubscriptionEmail(s, property);
                }
            }

        }
    }
}
