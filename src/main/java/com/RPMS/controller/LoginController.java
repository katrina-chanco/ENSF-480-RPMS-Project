package com.RPMS.controller;

import com.RPMS.model.entity.Account;
import com.RPMS.model.entity.Email;
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
    private String registrationSelectedType;

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
            System.out.println("\n\nemail not found...\n\n");
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
                System.out.println("\n\naccount not found...\n\n");
            }
            if (account != null) {
                System.out.println(account.getClass());
                isLoggedIn = true;
            }
            em.close();
        }
    }

    /**
     * Saves account into the database
     * @param account
     */
    private void saveAccount(Account account) {
        em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(account);
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

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isLoggedInUnregisteredRenter() {
        return isLoggedInUnregisteredRenter;
    }

    public Account getAccount() {
        return account;
    }

    public String getRegistrationSelectedType() {
        return registrationSelectedType;
    }

    public void setRegistrationSelectedType(String registrationSelectedType) {
        this.registrationSelectedType = registrationSelectedType;
    }

}
