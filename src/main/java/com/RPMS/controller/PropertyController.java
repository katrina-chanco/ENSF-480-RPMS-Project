package com.RPMS.controller;

import com.RPMS.model.entity.Landlord;
import com.RPMS.model.entity.Property;

import javax.persistence.*;
import java.util.ArrayList;
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

    public void saveProperty(Property property) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.persist(property);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
