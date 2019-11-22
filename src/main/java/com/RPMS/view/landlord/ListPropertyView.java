
package com.RPMS.view.landlord;

import com.RPMS.MainView;
import com.RPMS.controller.LoginController;
import com.RPMS.controller.landlord.LandlordController;
import com.RPMS.model.entity.*;
import com.RPMS.view.helpers.GridHelpers;
import com.RPMS.view.login_registration.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//TODO Check for landlord account
@Route(value = "landlord/list", layout = MainView.class)
@StyleSheet("./styles/badge.css")
public class ListPropertyView extends Div implements BeforeEnterObserver {
    private LandlordController landlordController = LandlordController.getInstance();
    /**
     * Add property dialog
     */
    private Dialog addPropertyDialog;
    private Dialog viewPropertyDialog;
    /**
     * List of landlord properties
     */
    private List<Property> properties;
    /**
     * Vaadin grid to display
     */
    private Grid<Property> propertyGrid = new Grid<>(Property.class);

    public ListPropertyView(){
        Button addPropertyButton = new Button("Add Property");
        addPropertyButton.addClickListener(e -> {
            addPropertyDialog = new LandlordAddPropertyDialog();
            addPropertyDialog.open();
            addPropertyDialog.addOpenedChangeListener( closeE-> {
                updateGrid();
            });
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
        propertyGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        propertyGrid.addItemClickListener(this::showGridEditDialog);
        Grid.Column<Property> addressColumn = propertyGrid.addColumn(Property::getAddress).setWidth("250px").setHeader("Address").setSortable(true);
        propertyGrid.addColumn(GridHelpers.getBathrooms()).setAutoWidth(true).setHeader("Bathrooms").setSortable(true).setComparator(Comparator.comparingDouble(Property::getBathrooms));
        propertyGrid.addColumn(GridHelpers.getBeds()).setAutoWidth(true).setHeader("Beds").setSortable(true).setComparator(Comparator.comparingDouble(Property::getBeds));
//        propertyGrid.addColumn(Property::getBathrooms).setAutoWidth(true).setHeader("Bathrooms").setSortable(true);
//        propertyGrid.addColumn(Property::getBeds).setAutoWidth(true).setHeader("Beds").setSortable(true);
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
        try {
            properties = landlordController.getAllLandlordProperties();
            propertyGrid.setItems(properties);
        } catch (NoResultException ex) {
            Notification.show("No results");
        }
    }

    private void showGridEditDialog(ItemClickEvent itemClickEvent) {
        Notification.show(itemClickEvent.getItem().toString());
        viewPropertyDialog = new LandlordViewPropertyDialog((Property) itemClickEvent.getItem());
        viewPropertyDialog.open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!LoginController.getInstance().isLoggedIn() && !LoginController.getInstance().isLoggedInUnregisteredRenter()) {
            beforeEnterEvent.rerouteTo(LoginView.class);
        }
    }
}
