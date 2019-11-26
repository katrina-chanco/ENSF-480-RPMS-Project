package com.RPMS.view.manager;

import com.RPMS.MainView;
import com.RPMS.controller.manager.ReportController;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(value = "manager/SelectSystemOptions/RequestReport", layout = MainView.class)
public class RequestReportView extends Div {
    private String reportName;
    private List<Object[]> displayReportList;
    private Grid<HashMap<String, String>> gridResult = new Grid<>();
    private List<HashMap<String, String>> reportDataCols = new ArrayList<>();
    private LocalDate startSelected;
    private LocalDate endSelected;
    private int dateCounter;

    public RequestReportView(){
        ReportController reportController = ReportController.getSingleInstance();
        Label instruction = new Label("Please Select a Date Range for Report");

        //Date for Period Ranges
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setLabel("Start Date");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setLabel("End Date");

        add(new VerticalLayout(
                new HorizontalLayout(instruction),
                new HorizontalLayout(startDatePicker, endDatePicker)
        ));

        startDatePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if (selectedDate != null) {
                endDatePicker.setMin(selectedDate.plusDays(1));
                if (endDate == null) {
                    endDatePicker.setOpened(true);
                    Notification.show("Select the ending date").setPosition(Notification.Position.BOTTOM_END);
                } else {
                    Notification.show(
                            "Selected period:\nFrom " + selectedDate.toString()
                                    + " to " + endDate.toString()).setPosition(Notification.Position.BOTTOM_END);
                    startSelected = selectedDate;
                    endSelected = endDate;
                    System.out.println(endDate);
                }
            } else {
                endDatePicker.setMin(null);
                Notification.show("Select the starting date").setPosition(Notification.Position.BOTTOM_END);
            }
        });

        endDatePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            LocalDate startDate = startDatePicker.getValue();
            if (selectedDate != null) {
                startDatePicker.setMax(selectedDate.minusDays(1));
                if (startDate != null) {
                    Notification.show(
                            "Selected period:\nFrom " + startDate.toString()
                                    + " to " + selectedDate.toString()).setPosition(Notification.Position.BOTTOM_END);
                    startSelected = startDate;
                    endSelected = selectedDate;
                } else {
                    Notification.show("Select the starting date").setPosition(Notification.Position.BOTTOM_END);
                }
            } else {
                startDatePicker.setMax(null);
                if (startDate != null) {
                    Notification.show("Select the ending date").setPosition(Notification.Position.BOTTOM_END);
                } else {
                    Notification.show("No date is selected").setPosition(Notification.Position.BOTTOM_END);
                }
            }
        });

        Button bTotalNumHousesListed = new Button("Total Number of Listed Houses");
        Button bTotalNumHousesRented = new Button("Total Number of Rented Houses");
        Button bTotalNumActiveListing = new Button("Total Number of Active Listings");
        Button bDescListHouseRented = new Button("Descriptive List of Houses Rented");

        add(new VerticalLayout(
                new HorizontalLayout(bTotalNumHousesListed, bTotalNumHousesRented, bTotalNumActiveListing, bDescListHouseRented),
                gridResult
        )) ;

        Label total = new Label();
        //Total Number of Houses Listed
        bTotalNumHousesListed.addClickListener(e-> {
            gridResult.removeAllColumns();
            reportDataCols = new ArrayList<>();
            dateCounter = 0;
            total.setText("");

            //Getting Report Specified
            Notification.show("Retrieving Report: Total Number of Houses Listed in Selected Period").setPosition(Notification.Position.BOTTOM_END);
            reportName = "Total_Num_Listed";
            displayReportList = reportController.generateReport(reportName);
            Notification.show("Report has been Received...").setPosition(Notification.Position.BOTTOM_END);

            //Columns for the grid view
            String PROP_ID = "Property ID";
            String BATHROOMS = "Number of Bathrooms";
            String BEDS = "Number of Beds";
            String DATE_ADDED = "Date Added";
            String PETS_ALLOWED = "Pet Status";
            String PRICE = "Price";
            String PROP_STATUS = "Property Status";
            String CITY = "City";
            String COUNTRY = "Country";
            String HOUSE_NM = "House Number";
            String POSTAL = "Postal Code";
            String PROVINCE = "Province";
            String STRTNAME = "Street Name";
            String FIRST_NAME = "LL First Name";
            String MID_NAME = "LL Middle Name";
            String LAST_NAME = "LL Last Name";

            //Organizing data to put into grid
            for(Object[] row : displayReportList){
                HashMap<String, String> fakeBean = new HashMap<>();
                String dataDateStr = row[3].toString();
                LocalDate dataDate = LocalDate.parse(dataDateStr);
                //Selecting data in specified Date Range
                if((dataDate.compareTo(startSelected) >= 0) && (dataDate.compareTo(endSelected) <= 0)){
                    fakeBean.put(PROP_ID, row[0].toString());
                    fakeBean.put(BATHROOMS, row[1].toString());
                    fakeBean.put(BEDS, row[2].toString());
                    fakeBean.put(DATE_ADDED, row[3].toString());
                    fakeBean.put(PETS_ALLOWED, row[4].toString());
                    fakeBean.put(PRICE, row[5].toString());
                    fakeBean.put(PROP_STATUS, row[6].toString());
                    fakeBean.put(CITY, row[9].toString());
                    fakeBean.put(COUNTRY, row[7].toString());
                    fakeBean.put(HOUSE_NM, row[12].toString());
                    fakeBean.put(POSTAL, row[10].toString());
                    fakeBean.put(PROVINCE, row[8].toString());
                    fakeBean.put(STRTNAME, row[11].toString());
                    fakeBean.put(FIRST_NAME, row[15].toString());
                    fakeBean.put(MID_NAME, row[14].toString());
                    fakeBean.put(LAST_NAME, row[13].toString());

                    reportDataCols.add(fakeBean);
                    //counts how many rows are being added to the grid based on Date Range
                    dateCounter++;
                }
            }
            gridResult.setItems(reportDataCols);

            //Adding data to the grid
            gridResult.addColumn(hm -> hm.get(PROP_ID)).setHeader(PROP_ID).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PROP_STATUS)).setHeader(PROP_STATUS).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PRICE)).setHeader(PRICE).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(DATE_ADDED)).setHeader(DATE_ADDED).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(BATHROOMS)).setHeader(BATHROOMS).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(BEDS)).setHeader(BEDS).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PETS_ALLOWED)).setHeader(PETS_ALLOWED).setSortable(true).setWidth("250px");

            gridResult.addColumn(hm -> hm.get(HOUSE_NM)).setHeader(HOUSE_NM).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(STRTNAME)).setHeader(STRTNAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(CITY)).setHeader(CITY).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PROVINCE)).setHeader(PROVINCE).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(POSTAL)).setHeader(POSTAL).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(COUNTRY)).setHeader(COUNTRY).setSortable(true).setWidth("250px");

            gridResult.addColumn(hm -> hm.get(FIRST_NAME)).setHeader(FIRST_NAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(MID_NAME)).setHeader(MID_NAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(LAST_NAME)).setHeader(LAST_NAME).setSortable(true).setWidth("250px");

            //Total number of properties listed
            String strLabel = String.valueOf(dateCounter);
            total.setText("Total Number of Properties Listed: " + strLabel);
        });

        //Total Number of Houses Rented
        bTotalNumHousesRented.addClickListener(e-> {
            gridResult.removeAllColumns();
            reportDataCols = new ArrayList<>();
            dateCounter = 0;
            total.setText("");

            //Getting Report Specified
            Notification.show("Retrieving Report: Total Number of Houses Rented in Selected Period").setPosition(Notification.Position.BOTTOM_END);
            reportName = "Total_Num_Rented";
            displayReportList = reportController.generateReport(reportName);
            Notification.show("Report has been Received...").setPosition(Notification.Position.BOTTOM_END);

            //Columns for the grid view
            String PROP_ID = "Property ID";
            String BATHROOMS = "Number of Bathrooms";
            String BEDS = "Number of Beds";
            String DATE_ADDED = "Date Added";
            String PETS_ALLOWED = "Pet Status";
            String PRICE = "Price";
            String PROP_STATUS = "Property Status";
            String CITY = "City";
            String COUNTRY = "Country";
            String HOUSE_NM = "House Number";
            String POSTAL = "Postal Code";
            String PROVINCE = "Province";
            String STRTNAME = "Street Name";
            String FIRST_NAME = "LL First Name";
            String MID_NAME = "LL Middle Name";
            String LAST_NAME = "LL Last Name";

            //Organizing data to put into grid
            for(Object[] row : displayReportList){
                HashMap<String, String> fakeBean = new HashMap<>();

                String dataDateStr = row[3].toString();
                LocalDate dataDate = LocalDate.parse(dataDateStr);
                //Selecting data in specified Date Range
                if((dataDate.compareTo(startSelected) >= 0) && (dataDate.compareTo(endSelected) <= 0)){
                    fakeBean.put(PROP_ID, row[0].toString());
                    fakeBean.put(BATHROOMS, row[1].toString());
                    fakeBean.put(BEDS, row[2].toString());
                    fakeBean.put(DATE_ADDED, row[3].toString());
                    fakeBean.put(PETS_ALLOWED, row[4].toString());
                    fakeBean.put(PRICE, row[5].toString());
                    fakeBean.put(PROP_STATUS, row[6].toString());
                    fakeBean.put(CITY, row[9].toString());
                    fakeBean.put(COUNTRY, row[7].toString());
                    fakeBean.put(HOUSE_NM, row[12].toString());
                    fakeBean.put(POSTAL, row[10].toString());
                    fakeBean.put(PROVINCE, row[8].toString());
                    fakeBean.put(STRTNAME, row[11].toString());
                    fakeBean.put(FIRST_NAME, row[15].toString());
                    fakeBean.put(MID_NAME, row[14].toString());
                    fakeBean.put(LAST_NAME, row[13].toString());

                    reportDataCols.add(fakeBean);
                    //counts how many rows are being added to the grid based on Date Range
                    dateCounter++;
                }
            }
            gridResult.setItems(reportDataCols);

            //Adding data to the grid
            gridResult.addColumn(hm -> hm.get(PROP_ID)).setHeader(PROP_ID).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PROP_STATUS)).setHeader(PROP_STATUS).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PRICE)).setHeader(PRICE).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(DATE_ADDED)).setHeader(DATE_ADDED).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(BATHROOMS)).setHeader(BATHROOMS).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(BEDS)).setHeader(BEDS).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PETS_ALLOWED)).setHeader(PETS_ALLOWED).setSortable(true).setWidth("250px");

            gridResult.addColumn(hm -> hm.get(HOUSE_NM)).setHeader(HOUSE_NM).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(STRTNAME)).setHeader(STRTNAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(CITY)).setHeader(CITY).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PROVINCE)).setHeader(PROVINCE).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(POSTAL)).setHeader(POSTAL).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(COUNTRY)).setHeader(COUNTRY).setSortable(true).setWidth("250px");

            gridResult.addColumn(hm -> hm.get(FIRST_NAME)).setHeader(FIRST_NAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(MID_NAME)).setHeader(MID_NAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(LAST_NAME)).setHeader(LAST_NAME).setSortable(true).setWidth("250px");

            //Total number of properties rented
            String strLabel = String.valueOf(dateCounter);
            total.setText("Total Number of Properties Listed: " + strLabel);
        });

        //Total Number of Houses Active
        bTotalNumActiveListing.addClickListener(e-> {
            gridResult.removeAllColumns();
            reportDataCols = new ArrayList<>();
            total.setText("");

            //Getting Report Specified
            Notification.show("Retrieving Report: Total Number of Active Listings").setPosition(Notification.Position.BOTTOM_END);
            reportName = "Total_Num_Active";
            displayReportList = reportController.generateReport(reportName);
            Notification.show("Report has been Received...").setPosition(Notification.Position.BOTTOM_END);

            //Columns for the grid view
            String PROP_ID = "Property ID";
            String BATHROOMS = "Number of Bathrooms";
            String BEDS = "Number of Beds";
            String DATE_ADDED = "Date Added";
            String PETS_ALLOWED = "Pet Status";
            String PRICE = "Price";
            String PROP_STATUS = "Property Status";
            String CITY = "City";
            String COUNTRY = "Country";
            String HOUSE_NM = "House Number";
            String POSTAL = "Postal Code";
            String PROVINCE = "Province";
            String STRTNAME = "Street Name";
            String FIRST_NAME = "LL First Name";
            String MID_NAME = "LL Middle Name";
            String LAST_NAME = "LL Last Name";

            //Organizing data to put into grid
            for(Object[] row : displayReportList){
                HashMap<String, String> fakeBean = new HashMap<>();
                fakeBean.put(PROP_ID, row[0].toString());
                fakeBean.put(BATHROOMS, row[1].toString());
                fakeBean.put(BEDS, row[2].toString());
                fakeBean.put(DATE_ADDED, row[3].toString());
                fakeBean.put(PETS_ALLOWED, row[4].toString());
                fakeBean.put(PRICE, row[5].toString());
                fakeBean.put(PROP_STATUS, row[6].toString());
                fakeBean.put(CITY, row[9].toString());
                fakeBean.put(COUNTRY, row[7].toString());
                fakeBean.put(HOUSE_NM, row[12].toString());
                fakeBean.put(POSTAL, row[10].toString());
                fakeBean.put(PROVINCE, row[8].toString());
                fakeBean.put(STRTNAME, row[11].toString());
                fakeBean.put(FIRST_NAME, row[15].toString());
                fakeBean.put(MID_NAME, row[14].toString());
                fakeBean.put(LAST_NAME, row[13].toString());

                reportDataCols.add(fakeBean);
            }
            gridResult.setItems(reportDataCols);

            //Adding data to the grid
            gridResult.addColumn(hm -> hm.get(PROP_ID)).setHeader(PROP_ID).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PROP_STATUS)).setHeader(PROP_STATUS).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PRICE)).setHeader(PRICE).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(DATE_ADDED)).setHeader(DATE_ADDED).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(BATHROOMS)).setHeader(BATHROOMS).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(BEDS)).setHeader(BEDS).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PETS_ALLOWED)).setHeader(PETS_ALLOWED).setSortable(true).setWidth("250px");

            gridResult.addColumn(hm -> hm.get(HOUSE_NM)).setHeader(HOUSE_NM).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(STRTNAME)).setHeader(STRTNAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(CITY)).setHeader(CITY).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PROVINCE)).setHeader(PROVINCE).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(POSTAL)).setHeader(POSTAL).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(COUNTRY)).setHeader(COUNTRY).setSortable(true).setWidth("250px");

            gridResult.addColumn(hm -> hm.get(FIRST_NAME)).setHeader(FIRST_NAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(MID_NAME)).setHeader(MID_NAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(LAST_NAME)).setHeader(LAST_NAME).setSortable(true).setWidth("250px");

            //Total number of properties active
            int sizeLabel = displayReportList.size();
            String strLabel = String.valueOf(sizeLabel);
            total.setText("Total Number of Properties Active: " + strLabel);
        });

        //Description of Houses Rented
        bDescListHouseRented.addClickListener(e-> {
            gridResult.removeAllColumns();
            reportDataCols = new ArrayList<>();
            dateCounter = 0;
            total.setText("");

            //Getting Report Specified
            Notification.show("Retrieving Report: Descriptive List of Houses Rented").setPosition(Notification.Position.BOTTOM_END);
            reportName = "Descriptive_List_Rented";
            displayReportList = reportController.generateReport(reportName);
            Notification.show("Report has been Received...").setPosition(Notification.Position.BOTTOM_END);

            //Columns for the grid view
            String HOUSE_ID = "House ID";
            String CITY = "City";
            String COUNTRY = "Country";
            String HOUSE_NM = "House Number";
            String POSTAL = "Postal Code";
            String PROVINCE = "Province";
            String STRTNAME = "Street Name";
            String FIRST_NAME = "LL First Name";
            String MID_NAME = "LL Middle Name";
            String LAST_NAME = "LL Last Name";

            //Organizing data to put into grid
            for(Object[] row : displayReportList){
                HashMap<String, String> fakeBean = new HashMap<>();

                String dataDateStr = row[2].toString();
                LocalDate dataDate = LocalDate.parse(dataDateStr);
                //Selecting data in specified Date Range
                if((dataDate.compareTo(startSelected) >= 0) && (dataDate.compareTo(endSelected) <= 0)){
                    fakeBean.put(HOUSE_ID, row[0].toString());
                    fakeBean.put(HOUSE_NM, row[3].toString());
                    fakeBean.put(STRTNAME, row[4].toString());
                    fakeBean.put(POSTAL, row[5].toString());
                    fakeBean.put(CITY, row[6].toString());
                    fakeBean.put(PROVINCE, row[7].toString());
                    fakeBean.put(COUNTRY, row[8].toString());
                    fakeBean.put(FIRST_NAME, row[9].toString());
                    fakeBean.put(MID_NAME, row[10].toString());
                    fakeBean.put(LAST_NAME, row[11].toString());

                    reportDataCols.add(fakeBean);
                    //counts how many rows are being added to the grid based on Date Range
                    dateCounter++;
                }
            }
            gridResult.setItems(reportDataCols);

            //Adding data to the grid
            gridResult.addColumn(hm -> hm.get(FIRST_NAME)).setHeader(FIRST_NAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(MID_NAME)).setHeader(MID_NAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(LAST_NAME)).setHeader(LAST_NAME).setSortable(true).setWidth("250px");

            gridResult.addColumn(hm -> hm.get(HOUSE_ID)).setHeader(HOUSE_ID).setSortable(true).setWidth("250px");

            gridResult.addColumn(hm -> hm.get(HOUSE_NM)).setHeader(HOUSE_NM).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(STRTNAME)).setHeader(STRTNAME).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(POSTAL)).setHeader(POSTAL).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(CITY)).setHeader(CITY).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(PROVINCE)).setHeader(PROVINCE).setSortable(true).setWidth("250px");
            gridResult.addColumn(hm -> hm.get(COUNTRY)).setHeader(COUNTRY).setSortable(true).setWidth("250px");
        });

        add(new VerticalLayout(
                new HorizontalLayout(total)
        ));
    }
}

