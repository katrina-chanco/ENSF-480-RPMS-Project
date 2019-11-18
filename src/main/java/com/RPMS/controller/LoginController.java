package com.RPMS.controller;

import com.RPMS.model.entity.Account;
import com.vaadin.flow.component.login.AbstractLogin;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class LoginController {
    @PersistenceContext
    /**
     * Allows use of querying in JPA
     */
            EntityManager em;
    /**
     * Static instance of LoginController
     */
    private static LoginController obj;
    /**
     * Account currently assigned to the controlelr
     */
    private Account account;
    /**
     * Shows if there is a user currently logged in
     */
    private Boolean isLoggedIn;

    /**
     * Default constructor
     */
    private LoginController() {
        isLoggedIn = false;
        account = null;
    }

    /**
     * getInstance method for the Singleton pattern
     *
     * @return instance of LoginController
     */
    public static LoginController getInstance() {
        if (obj == null) {
            obj = new LoginController();
        }
        return obj;
    }

    /**
     * Validates credentials entered by user to login
     */
    public void login(AbstractLogin.LoginEvent e) {
        TypedQuery<Account> query = em.createNamedQuery("Account.validateLogin", Account.class);
        account = query.setParameter("email", e.getUsername()).setParameter("password", e.getPassword()).getSingleResult();
        if (account != null) {
            isLoggedIn = true;
        } else {
            isLoggedIn = false;
        }
    }

    /**
     * Logs the user out
     */
    public void logOut() {
        if (isLoggedIn) {
            account = null;
            isLoggedIn = false;
        }
    }

}
