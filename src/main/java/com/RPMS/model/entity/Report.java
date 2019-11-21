package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "Report.searchByName",  query = "SELECT r.query FROM Report r WHERE r.name = :name")
})
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String query;

    public Report(String name, String query) {
        this.name = name;
        this.query = query;
    }

    public Report(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
