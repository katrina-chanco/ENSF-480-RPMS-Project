package com.RPMS.controller.manager;

import com.RPMS.model.entity.Account;
import com.RPMS.model.entity.SystemSetting;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class AccountController {
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RPMS_PU");
    private EntityManager entityManager;
    private static AccountController singleInstance;

    private AccountController() {
    }

    public static AccountController getInstance() {
        if (singleInstance == null) {
            singleInstance = new AccountController();
        }
        return singleInstance;
    }

    public List<Account> getAccount(){
        entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Account> queryAccount = entityManager.createNamedQuery("Account.findAll", Account.class);
        List<Account> accountList = queryAccount.getResultList();
        entityManager.close();
        return accountList;
    }
}
