package com.RPMS.controller;

import com.RPMS.model.entity.*;
import com.vaadin.flow.component.login.AbstractLogin;

import javax.persistence.*;

public class LoginController {
    /**
     * Static instance of LoginController
     */
    private static LoginController obj;
    /**
     * Allows use of querying in JPA
     */
    @PersistenceContext
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RPMS_PU");
    private EntityManager em;
    /**
     * Account currently assigned to the controller
     */
    private Account account;
    /**
     * Shows if there is a user currently logged in
     */
    private Boolean isLoggedIn;
    private Boolean isLoggedInUnregisteredRenter;

    /**
     * Default constructor
     */
    private LoginController() {
        isLoggedIn = false;
        isLoggedInUnregisteredRenter = false;
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
     * Logs the user in as an unregistered renter
     *
     * @return
     */
    public boolean loginAsUnregisteredRenter() {
        account = null;
        isLoggedIn = false;
        isLoggedInUnregisteredRenter = true;
        return isLoggedInUnregisteredRenter;
    }

    /**
     * Validates credentials entered by user to login
     */
    public boolean authenticateUser(AbstractLogin.LoginEvent e) {
        Email email = findEmailByAddress(e.getUsername());
        loginAccount(email, e.getPassword());
        return isLoggedIn;
    }

    public String getAccountType() {
        if (account == null && isLoggedInUnregisteredRenter()) {
            return "UnregisteredRenter";
        }
        if (LoginController.getInstance().getAccount().getClass() == Landlord.class) {
            return "Landlord";
        }
        if (LoginController.getInstance().getAccount().getClass() == Registered_Renter.class) {
            return "Registered_Renter";
        }
        if (LoginController.getInstance().getAccount().getClass() == Manager.class) {
            return "Manager";
        }
        return null;
    }


    /**
     * Finds the user using their email
     * @param emailAddress
     * @return
     */
    private Email findEmailByAddress(String emailAddress) {
        em = entityManagerFactory.createEntityManager();
        Email email = null;
        TypedQuery<Email> query = em.createNamedQuery("Email.findEmail", Email.class);
        try {
            email = query.setParameter("emailAddress", emailAddress).getSingleResult();
        } catch (NullPointerException | NoResultException e) {
            e.printStackTrace();
        }
        em.close();
        return email;
    }

    /**
     * Logs in the account that has been registered
     * @param account
     */
    public void loginRegistrationAccount(Account account) {
        this.account = account;
        saveAccount(account);
        isLoggedIn = true;
    }

    /**
     * Logs in a user and validates depending on their input
     * @param email
     * @param password
     */
    private void loginAccount(Email email, String password) {
        if (email != null) {
            em = entityManagerFactory.createEntityManager();
            TypedQuery<Account> query = em.createNamedQuery("Account.validateLogin", Account.class);
            try {
                account = query.setParameter("email", email).setParameter("password", password).getSingleResult();
            } catch (NullPointerException | NoResultException e) {
                e.printStackTrace();
            }
            if (account != null) {
                isLoggedIn = true;
            }
            em.close();
        }
    }

    /**
     * Saves account into the database
     * @param account
     */
    public void saveAccount(Account account) {
        em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
        em.close();
    }

    public void mergeAccount(Account account) {
        em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.merge(account);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Logs the user out
     */
    public void logOutUser() {
        if (isLoggedIn || isLoggedInUnregisteredRenter) {
            account = null;
            isLoggedIn = false;
            isLoggedInUnregisteredRenter = false;
        }
    }

    /**
     * Gets email of user currently logged in
     *
     * @return
     */
    public String getUserEmail() {
        LoginController loginCont = LoginController.getInstance();
        if (loginCont.isLoggedIn()) {
            return loginCont.getAccount().getEmail().getEmailAddress();
        }
        return "";
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isLoggedInUnregisteredRenter() {
        return isLoggedInUnregisteredRenter;
    }

    public Account getAccount() {
        return account;
    }


}
