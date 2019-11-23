package com.RPMS.controller;

import com.RPMS.model.entity.SystemSetting;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class SystemSettingController {
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RPMS_PU");
    private EntityManager entityManager;

    /**
     * Singleton pattern
     */
    private static SystemSettingController instance;

    private SystemSettingController() {
    }

    public static SystemSettingController getInstance() {
        if (instance == null) {
            instance = new SystemSettingController();
        }
        return instance;
    }

    /**
     * Public helper to fins system setting
     * @param name name of setting
     * @return
     */
    public SystemSetting getSettingByName(String name) {
        entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SystemSetting> query =
                entityManager.createNamedQuery("SystemSetting.findByName", SystemSetting.class).setParameter("name", name);
        SystemSetting setting = query.getSingleResult();
        entityManager.close();
        return setting;
    }

    public Integer getSubscriptionLength() {
        SystemSetting daysSetting = getSettingByName("sub_days");
        return Integer.valueOf(daysSetting.getSettingValue());
    }

    public Double getSubscriptionCost() {
        SystemSetting costSetting = getSettingByName("sub_amount");
        return Double.valueOf(costSetting.getSettingValue());
    }

}
