package com.RPMS;

import com.RPMS.model.Name;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.router.Route;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


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
                    Name newName = new Name("Evan", "John", "Krul");

                    em.persist(newName);
                    em.getTransaction().commit();
                    em.close();
                });
        add(button);




    }
}
