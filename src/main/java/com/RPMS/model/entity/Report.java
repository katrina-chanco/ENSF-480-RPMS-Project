package com.RPMS.model.entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        //Interacts with Database to get Report Type
        @NamedQuery(name = "Report.searchByName",  query = "SELECT r FROM Report r WHERE r.name = :name")
})
@Table
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
