package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String query;
    private String name;

    public Report(String query, String name) {
        this.query = query;
        this.name = name;
    }

    public Report(String query) {
        this.query = query;
    }

    public Report(){

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
