package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String query;

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
}
