
package com.RPMS.view.landlord;

import com.RPMS.MainView;
import com.RPMS.controller.landlord.LandlordController;
import com.RPMS.model.entity.*;
import com.RPMS.view.helpers.GridHelpers;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//TODO Check for landlord account
@Route(value = "landlord/list", layout = MainView.class)
@StyleSheet("./styles/badge.css")
public class ListPropertyView extends Div {
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RPMS_PU");
    private LandlordController landlordController = LandlordController.getInstance();
    /**
     * Add property dialog
     */
    private Dialog addPropertyDialog;
    /**
     * List of landlord properties
     */
    private List<Property> properties;
    /**
     * Vaadin grid to display
     */
    private Grid<Property> propertyGrid = new Grid<>(Property.class);

    public ListPropertyView() {
//        Button button = new Button("Populate landlord button");
////
//        button.addClickListener(e -> {
////            TODO use login  controller
//            EntityManager em = entityManagerFactory.createEntityManager();
////            TypedQuery<Account> landlordTypedQuery = em.createNamedQuery("Account.findByEmail", Account.class).setParameter("email", "email@email.ca");
//            try {
////                Landlord landlord = (Landlord) landlordTypedQuery.getSingleResult();
////                List<Property> properties = landlordController.getAllLandlordProperties(landlord);
////                System.out.println(properties);
////
//                em.getTransaction().begin();
//
////                Property property = new Property();
////                property.setAddress(new Address(2, "Street", "City", "Postal", "Alberta", "Canada"));
////                property.setLandlord(landlord);
////                property.setBathrooms(3);
////                property.setBeds(1);
////                ArrayList<Amenity> amm = new ArrayList<>();
////                amm.add(new Amenity("AC"));
////                amm.add(new Amenity("Water"));
////                amm.add(new Amenity("Heat"));
////                amm.add(new Amenity("Utils"));
////                property.setAmenities(amm);
////                property.setPetsAllowed(Property.Pets_Allowed.DOGS_ALLOWED);
////                property.setPrice(2333);
//
//
//                Account account = new Landlord(new Address(1, "Street", "City", "Postal", "AB", "Canada"),
//                        new Name("F", "M", "L"),
//                        new Email("email@email.ca"), null, null);
//
//
//                em.persist(account);
//                em.getTransaction().commit();
////                Notification.show(landlord.getEmail().getEmailAddress());
//
//
//            } catch (NoResultException error) {
//                Notification.show("Query error");
//                System.err.println(error.getMessage());
//            } catch (Exception error) {
//                Notification.show("error");
//                error.printStackTrace(System.out);
//            }
//
//
//        });
//
//        Label label = new Label("END TESTS");
//        add(new VerticalLayout(button, label));

        Button addPropertyButton = new Button("Add Property");
        addPropertyButton.addClickListener(e -> {
            addPropertyDialog = new LandlordAddPropertyDialog();
            addPropertyDialog.open();
        });
        buildPropertyGrid();
        add(new VerticalLayout(addPropertyButton, propertyGrid));
    }

    /**
     * Build grid UI
     */
    private void buildPropertyGrid() {
        updateGrid();
        propertyGrid.removeAllColumns();
        Grid.Column<Property> addressColumn = propertyGrid.addColumn(Property::getAddress).setWidth("250px").setHeader("Address").setSortable(true);
        propertyGrid.addColumn(Property::getBathrooms).setAutoWidth(true).setHeader("Bathrooms").setSortable(true);
        propertyGrid.addColumn(Property::getBeds).setAutoWidth(true).setHeader("Beds").setSortable(true);
        propertyGrid.addColumn(GridHelpers.getPropertyPetBadge()).setAutoWidth(true).setHeader("Pets Allowed");
        propertyGrid.addColumn(GridHelpers.getPropertyCost()).setAutoWidth(true).setHeader("Price").setSortable(true).setComparator(Comparator.comparingDouble(Property::getPrice));
        propertyGrid.addColumn(GridHelpers.getPropertyContract()).setAutoWidth(true).setHeader("Contract");
        propertyGrid.addColumn(GridHelpers.getPropertyStatusBadge()).setHeader("Status");
        Grid.Column<Property> rightColumn = propertyGrid.addColumn(Property::getDateAdded).setAutoWidth(true).setHeader("Creation Date").setSortable(true);


//            Filter address
        HeaderRow filterRow = propertyGrid.appendHeaderRow();

        TextField addressTextFilter = new TextField();
        addressTextFilter.addValueChangeListener(e -> {
            List<Property> filteredItemList = properties.stream().filter(property -> property.getAddress().toString().contains(addressTextFilter.getValue())).collect(Collectors.toList());
            propertyGrid.setItems(filteredItemList);
        });

        addressTextFilter.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(addressColumn).setComponent(addressTextFilter);
        addressTextFilter.setSizeFull();
        addressTextFilter.setClearButtonVisible(true);
        addressTextFilter.setPlaceholder("Filter...");

        Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
        refreshButton.addClickListener(e->{
            updateGrid();
        });
        filterRow.getCell(rightColumn).setComponent(refreshButton);
    }

    /**
     * Update grid with db info
     */
    private void updateGrid() {
        //      TODO: use login controller
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<Account> landlordTypedQuery = em.createNamedQuery("Account.findByEmail", Account.class).setParameter("email", "email@email.ca");
        try {
            Landlord landlord = (Landlord) landlordTypedQuery.getSingleResult();
            properties = landlordController.getAllLandlordProperties(landlord);
            propertyGrid.setItems(properties);
        } catch (NoResultException ex) {
            Notification.show("No results");
        }
    }
}
