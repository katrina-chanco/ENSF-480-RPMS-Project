package com.RPMS.controller.manager;

import com.RPMS.model.entity.Account;
import com.RPMS.model.entity.Property;
import com.RPMS.model.entity.Report;
import com.RPMS.view.manager.RequestReportView;

import javax.management.Notification;
import javax.persistence.*;
import java.util.List;

public class ReportController {
    private static ReportController singleInstance;
//    private Account manager;
//    private RequestReportView reportView;
//    private List<Report> reports;

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RPMS_PU");
    private EntityManager entityManager;

    //Report Object has String Query

    //Total Num Houses listed in period
    //Total Num Houses rented in the period
    //Total Num Active Listings
    //List of Houses rented in the period
        //Displays:
        //Landlord Name
        //House ID Number
        //Address of House

    private ReportController(){
    }

    public static ReportController getSingleInstance(){
        if(singleInstance == null){
            singleInstance = new ReportController();
        }
        return singleInstance;
    }

    public static void setSingleInstance(ReportController singleInstance){
        ReportController.singleInstance = singleInstance;

    }

    public void retrieveReport(Account account, Report report){

    }

    public String generateReport(String reportType){
        //String reportQueryName = "Report." + reportType;
        entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Report> reportQuery = entityManager.createNamedQuery("Report.searchByName", Report.class).setParameter("name", reportType);
        Report resultReportType = reportQuery.getSingleResult();
        entityManager.close();
        String strTableQuery = resultReportType.getQuery();
        System.out.println("GOT THE STRING");

        Query tableQuery = entityManager.createNamedQuery(strTableQuery);
        List resultReportValues = tableQuery.getResultList();
        return resultReportValues.get(0).toString();
       // TypedQuery<Property>  propertyQuery = em.createNamedQuery("Report.", Property.class).setParameter("name", resultReport.getQuery());

        // TODO run this
        //result.getQuery();
    }
//
//    public void addReport(Report oneReport){
//        reports.add(oneReport);
//    }
//
//    public Account getManager() {
//        return manager;
//    }
//
//    public void setManager(Account manager) {
//        this.manager = manager;
//    }
//
//    public RequestReportView getReportView() {
//        return reportView;
//    }
//
//    public void setReportView(RequestReportView reportView) {
//        this.reportView = reportView;
//    }
//
//    public List<Report> getReports() {
//        return reports;
//    }
//
//    public void setReports(List<Report> reports) {
//        this.reports = reports;
//    }
}
