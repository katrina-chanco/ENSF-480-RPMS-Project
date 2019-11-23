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
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RPMS_PU");
    private EntityManager entityManager;

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

    //Interacts with Database and uses Report Type Query to get Report
    private List<Object[]> retrieveReport(String query){
        entityManager = entityManagerFactory.createEntityManager();
        Query q = entityManager.createNativeQuery(query);
        List<Object[]> results = q.getResultList();
        entityManager.close();
        return results;
    }

    //Interacts with Report to get selected type of report
    public List<Object[]> generateReport(String reportType){
        entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Report> reportQuery = entityManager.createNamedQuery("Report.searchByName", Report.class).setParameter("name", reportType);
        Report resultReportType = reportQuery.getSingleResult();
        entityManager.close();
        String strTableQuery = resultReportType.getQuery();
        return retrieveReport(strTableQuery);
    }
}
