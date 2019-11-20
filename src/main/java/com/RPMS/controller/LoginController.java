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
    private Boolean isLoggedInAsTemporaryUser;

    /**
     * Default constructor
     */
    private LoginController() {
        isLoggedIn = false;
        isLoggedInAsTemporaryUser = false;
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

    public boolean loginAsUnregisteredRenter() {
        account = null;
        isLoggedIn = false;
        isLoggedInAsTemporaryUser = true;
        return isLoggedInAsTemporaryUser;

    }

    /**
     * Validates credentials entered by user to login
     */
    public boolean authenticateUser(AbstractLogin.LoginEvent e) {
        TypedQuery<Account> query = em.createNamedQuery("Account.validateLogin", Account.class);
        account = query.setParameter("email", e.getUsername()).setParameter("password", e.getPassword()).getSingleResult();
        isLoggedIn = account != null;
        return isLoggedIn;
    }

    /**
     * Logs the user out
     */
    public void logOutUser() {
        if (isLoggedIn || isLoggedInAsTemporaryUser) {
            account = null;
            isLoggedIn = false;
            isLoggedInAsTemporaryUser = false;
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isLoggedInAsTemporaryUser() {
        return isLoggedInAsTemporaryUser;
    }

    public void registerUser(AbstractLogin.ForgotPasswordEvent e) {
        //TODO
    }

    public Account getAccount() {
        return account;
    }
}
