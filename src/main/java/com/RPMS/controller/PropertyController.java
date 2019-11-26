package com.RPMS.controller;

import com.RPMS.model.entity.Landlord;
import com.RPMS.model.entity.Property;

import javax.persistence.*;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;

public class PropertyController {
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RPMS_PU");
    private EntityManager entityManager;

    /**
     * Singleton pattern
     */
    private static PropertyController instance;

    private PropertyController() {
    }

    public static PropertyController getInstance() {
        if (instance == null) {
            instance = new PropertyController();
        }
        return instance;
    }

    public List<Property> getAllLandlordProperties(Landlord landlord) {
        entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Property> query =
                entityManager.createNamedQuery("Property.findAllForLandlord", Property.class).setParameter("landlord", landlord);
        List<Property> list = query.getResultList();
        entityManager.close();
        return list;
    }

    public List<Property> getAllProperties(Map<String, Object> queryParams) {
        entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Property> query =entityManager.createNamedQuery("Property.findAllByQuery", Property.class);
        query.setParameter("lowerPrice", queryParams.get("lowerPrice"));
        query.setParameter("upperPrice", queryParams.get("upperPrice"));
        query.setParameter("lowerBed", queryParams.get("lowerBed"));
        query.setParameter("upperBed", queryParams.get("upperBed"));
        query.setParameter("lowerBath", queryParams.get("lowerBath"));
        query.setParameter("upperBath", queryParams.get("upperBath"));
        query.setParameter("city", queryParams.get("city"));
        query.setParameter("province", queryParams.get("province"));
        query.setParameter("postal", queryParams.get("postal"));
        query.setParameter("country", queryParams.get("country"));
        query.setParameter("pets_allowed", queryParams.get("pets_allowed"));

        List<Property> list = query.getResultList();
        entityManager.close();
        return list;
    }

    /**
     * Save/update property in DB
     * @param property property object
     */
    public void saveProperty(Property property) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(property);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * update property status
     * @param property
     * @param value
     */
    public void updateStatus(Property property, Property.Property_Status value) {
        property.setPropertyStatus(value);
        property.setDateActivated(new java.sql.Date(new Date().getTime()));
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(property);
        entityManager.getTransaction().commit();
        entityManager.close();
        SubscriptionController.getInstance().notifySubscribers(property);
    }

    /**
     * Helper for creating properties
     * @return
     */
    public Property generatePropertyBean() {
        Property property = new Property();
        property.setDateAdded(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        property.setContract(null);
//        Landlord will be populated in cntl
        property.setLandlord(null);
        property.setPropertyStatus(Property.Property_Status.SUSPENDED);
        return property;
    }

    public List<Property> getAllProperties() {
        entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Property> query = entityManager.createNamedQuery("Property.findAll", Property.class);
        List<Property> list = query.getResultList();
        entityManager.close();
        return list;
    }
}
