package com.RPMS;

import com.RPMS.model.entity.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.router.Route;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * The main view contains a button and a click listener.
 */
@Route
@PWA(name = "My Application", shortName = "My Application")
public class MainView extends VerticalLayout {

    private static EntityManagerFactory factory;

    public MainView() {
        Button button = new Button("Click me",
                event ->{
                    factory = Persistence.createEntityManagerFactory("RPMS_PU");
                    EntityManager em = factory.createEntityManager();
                    em.getTransaction().begin();

                    Account account = new Manager(new Address(1,"Street", "City", "Postal", "Canada"),
                            new Name("F", "M", "L"),
                            new Email("email@email.ca"), "TESTMAN");

                    em.persist(account);
                    em.getTransaction().commit();
                    em.close();
                });
        add(button);




    }
}
