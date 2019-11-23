package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
@NamedQuery(name = "SystemSetting.findByName", query = "SELECT s FROM SystemSetting s WHERE s.settingName = :name")
public class SystemSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String settingName;
    private String settingValue;


    public SystemSetting(String settingName, String settingValue) {
        this.settingName = settingName;
        this.settingValue = settingValue;
    }

    public SystemSetting() {

    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
}
