package com.RPMS.view.manager;

import com.RPMS.MainView;
import com.RPMS.controller.manager.ReportController;
import com.RPMS.model.entity.Report;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;

////        Button button = new Button("Click me",
////                event ->{
////                    factory = Persistence.createEntityManagerFactory("RPMS_PU");
////                    EntityManager em = factory.createEntityManager();
////                    em.getTransaction().begin();
////
////                    Account account = new Manager(new Address(1,"Street", "City", "Postal", "Canada"),
////                            new Name("F", "M", "L"),
////                            new Email("email@email.ca"), "TESTMAN");
////
////                    em.persist(account);
////                    em.getTransaction().commit();
////                    em.close();
////                });
////        add(button);

@Route(value = "manager/SelectSystemOptions/RequestReport", layout = MainView.class)
public class RequestReportView extends Div {
    private String reportName;
    private String displayReport;

    private List<Report> reportView;

    public RequestReportView(){
        ReportController reportController = ReportController.getSingleInstance();
        Label label = new Label("Displaying Request Report Page");
        add(label);

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setLabel("Start Date");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setLabel("End Date");
        add(startDatePicker, endDatePicker );

        startDatePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if (selectedDate != null) {
                endDatePicker.setMin(selectedDate.plusDays(1));
                if (endDate == null) {
                    endDatePicker.setOpened(true);
                    Notification.show("Select the ending date");
                } else {
                    Notification.show(
                    "Selected period:\nFrom " + selectedDate.toString()
                            + " to " + endDate.toString());
                }
            } else {
                endDatePicker.setMin(null);
                Notification.show("Select the starting date");
            }
        });

        endDatePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            LocalDate startDate = startDatePicker.getValue();
            if (selectedDate != null) {
                startDatePicker.setMax(selectedDate.minusDays(1));
                if (startDate != null) {
                    Notification.show(
                            "Selected period:\nFrom " + selectedDate.toString()
                                    + " to " + selectedDate.toString());
                } else {
                    Notification.show("Select the starting date");
                }
            } else {
                startDatePicker.setMax(null);
                if (startDate != null) {
                    Notification.show("Select the ending date");
                } else {
                    Notification.show("No date is selected");
                }
            }
        });

        Button bTotalNumHousesListed = new Button("Total Number of Listed Houses (In Selected Period)");
        Button bTotalNumHousesRented = new Button("Total Number of Rented Houses (In Selected Period)");
        Button bTotalNumActiveListing = new Button("Total Number of Active Listings");
        Button bDescListHouseRented = new Button("Descriptive List of Houses Rented (In Selected Period)");
        add(bTotalNumHousesListed, bTotalNumHousesRented, bTotalNumActiveListing, bDescListHouseRented) ;

        bTotalNumHousesListed.addClickListener(e-> {
            Notification.show("Retrieving Report: Total Number of Houses Listed in Selected Period");
            reportName = "Total_Num_Listed";
         //   displayReport = reportController.generateReport(reportName);
            Notification.show("Report has been Received...");
            Label displayLabel = new Label(displayReport);
            add(displayLabel);


//            reportView = reportController.getReports();
//            String repName = reportView.get(0).getName();
//            String repQuery = reportView.get(0).getQuery();



        });

        bTotalNumHousesRented.addClickListener(e-> {
            Notification.show("Retrieving Report: Total Number of Houses Rented in Selected Period");
            reportName = "Total_Num_Rented";
        });

        bTotalNumActiveListing.addClickListener(e-> {
            Notification.show("Retrieving Report: Total Number of Active Listings");
            reportName = "Total_Num_Active";
        });

        bDescListHouseRented.addClickListener(e-> {
            Notification.show("Retrieving Report: Descriptive List of Houses Rented");
            reportName = "Descriptive_List_Rented";
        });
    }
}
