package com.RPMS.view.renter;

import com.RPMS.MainView;
import com.RPMS.controller.LoginController;
import com.RPMS.controller.PropertyController;
import com.RPMS.model.entity.Property;
import com.RPMS.model.entity.Registered_Renter;
import com.RPMS.view.helpers.GridHelpers;
import com.RPMS.view.landlord.LandlordViewPropertyDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

import javax.persistence.NoResultException;
import java.util.*;

@StyleSheet("./styles/badge.css")
@Route(value = "renter/list_properties", layout = MainView.class)
public class RenterSearchPropertyView extends Div {
    private boolean isRegistered;
    private HorizontalLayout rootView;
    private VerticalLayout searchBox;
    private NumberField lowerPriceRange;
    private NumberField upperPriceRange;
    private NumberField lowerBedRange;
    private NumberField upperBedRange;
    private NumberField lowerBathRange;
    private NumberField upperBathRange;
    private TextField cityFilter;
    private TextField provinceFilter;
    private TextField postalFilter;
    private ComboBox<String> countryFilter;
    private Button filterButton;
    private ComboBox<Property.Pets_Allowed> petsAllowedComboBox;
    private Dialog viewPropertyDialog;
    private Button subscribeButton;
    private Button unsubscribeButton;
    private  Registered_Renter registered_renter;



    /**
     * List of landlord properties
     */
    private List<Property> properties;
    /**
     * Vaadin grid to display
     */
    private Grid<Property> propertyGrid = new Grid<>(Property.class);

    /**
     * Search map
     */
    private Map<String, Object> searchMap = new HashMap<>();

    public RenterSearchPropertyView() {
        isRegistered = LoginController.getInstance().isLoggedIn() && !LoginController.getInstance().isLoggedInUnregisteredRenter();
        if(isRegistered) {
             registered_renter = (Registered_Renter) LoginController.getInstance().getAccount();
        }
        rootView = new HorizontalLayout();
        searchBox = new VerticalLayout();
        searchBox.setWidth("300px");
        rootView.add(searchBox, propertyGrid);
        generateSearchBar();
        buildPropertyGrid();
        add(rootView);
    }

    private void generateSearchBar() {
        H3 searchTitle = new H3("Search For Properties");

        cityFilter = new TextField("City");
        cityFilter.setWidth("256px");
        cityFilter.setClearButtonVisible(true);
        postalFilter = new TextField("Postal Code/Zip");
        postalFilter.setWidth("256px");
        postalFilter.setClearButtonVisible(true);
        new CustomStringBlockFormatter(new int[]{3, 3}, new String[]{" "}, CustomStringBlockFormatter.ForceCase.UPPER, null, false).extend(postalFilter);

        provinceFilter = new TextField("Province/State");
        provinceFilter.setWidth("256px");
        provinceFilter.setClearButtonVisible(true);
        countryFilter = new ComboBox<>("Country");
        countryFilter.setWidth("256px");
        countryFilter.setItems("Canada", "USA");
        cityFilter.setClearButtonVisible(true);

        lowerPriceRange = new NumberField("Min Price");
        lowerPriceRange.setWidth("120px");
        lowerPriceRange.setClearButtonVisible(true);
        lowerPriceRange.setMin(0);
        upperPriceRange = new NumberField("Max Price");
        upperPriceRange.setWidth("120px");
        upperPriceRange.setClearButtonVisible(true);
        upperPriceRange.setMin(0);

        lowerBathRange = new NumberField("Min Baths");
        lowerBathRange.setWidth("120px");
        lowerBathRange.setClearButtonVisible(true);
        lowerBathRange.setMin(0);
        upperBathRange = new NumberField("Max Baths");
        upperBathRange.setWidth("120px");
        upperBathRange.setClearButtonVisible(true);
        upperBathRange.setMin(0);


        lowerBedRange = new NumberField("Min Beds");
        lowerBedRange.setWidth("120px");
        lowerBedRange.setClearButtonVisible(true);
        lowerBedRange.setMin(0);
        upperBedRange = new NumberField("Max Beds");
        upperBedRange.setWidth("120px");
        upperBedRange.setClearButtonVisible(true);
        upperBedRange.setMin(0);

        petsAllowedComboBox = new ComboBox<>("Pets Allowed");
        petsAllowedComboBox.setWidth("256px");
        petsAllowedComboBox.setClearButtonVisible(true);
        List<Property.Pets_Allowed> pets_allowedList = new ArrayList<>(EnumSet.allOf(Property.Pets_Allowed.class));

        petsAllowedComboBox.setDataProvider(new ListDataProvider<>(pets_allowedList));
        petsAllowedComboBox.setItemLabelGenerator(Property.Pets_Allowed::getPrettyName);

        filterButton = new Button("Apply Filter");
        filterButton.setThemeName(Lumo.DARK);
        filterButton.addClickListener(e->{
            buildSearchMap();
            updateGrid();
        });
        HorizontalLayout priceFilter = new HorizontalLayout(lowerPriceRange, upperPriceRange);
        HorizontalLayout bathFilter = new HorizontalLayout(lowerBathRange, upperBathRange);
        HorizontalLayout bedFilter = new HorizontalLayout(lowerBedRange, upperBedRange);

        renderSubscriptionButtons();

        searchBox.add(
                searchTitle,
                cityFilter,
                provinceFilter,
                postalFilter,
                countryFilter,
                priceFilter,
                bathFilter,
                bedFilter,
                petsAllowedComboBox,
                new HorizontalLayout(filterButton, subscribeButton, unsubscribeButton)
        );
    }

    /**
     * Build grid UI
     */
    private void buildPropertyGrid() {
        updateGrid();
        propertyGrid.removeAllColumns();
        propertyGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        propertyGrid.addItemClickListener(e -> Notification.show("NTI"));
        propertyGrid.addColumn(Property::getAddress).setWidth("400px").setHeader("Address").setSortable(true);
        propertyGrid.addColumn(GridHelpers.getBathrooms()).setAutoWidth(true).setHeader("Bathrooms").setSortable(true).setComparator(Comparator.comparingDouble(Property::getBathrooms));
        propertyGrid.addColumn(GridHelpers.getBeds()).setAutoWidth(true).setHeader("Beds").setSortable(true).setComparator(Comparator.comparingDouble(Property::getBeds));
        propertyGrid.addColumn(GridHelpers.getPropertyPetBadge()).setAutoWidth(true).setHeader("Pets Allowed");
        propertyGrid.addColumn(GridHelpers.getPropertyCost()).setAutoWidth(true).setHeader("Price").setSortable(true).setComparator(Comparator.comparingDouble(Property::getPrice));
        propertyGrid.addColumn(Property::getDateActivated).setAutoWidth(true).setHeader("Listed Date").setSortable(true);
        propertyGrid.setHeight("850px");
        propertyGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        propertyGrid.addItemClickListener(this::showGridEditDialog);

        Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
        refreshButton.addClickListener(e -> {
            updateGrid();
        });
    }

    /**
     * Update grid with db info
     */
    private void updateGrid() {
        try {
            properties = PropertyController.getInstance().getAllProperties(searchMap);
            propertyGrid.setItems(properties);
        } catch (NoResultException ex) {
            Notification.show("No results");
        }
    }

    private void buildSearchMap() {
        searchMap.put("lowerPrice", lowerPriceRange.getValue());
        searchMap.put("upperPrice", upperPriceRange.getValue());

        try {
            searchMap.put("lowerBath", lowerBathRange.getValue().intValue());
        } catch (NullPointerException ignored){}
        try {
            searchMap.put("upperBath", upperBathRange.getValue().intValue());
        } catch (NullPointerException ignored){}

        try {
            searchMap.put("lowerBed", lowerBedRange.getValue().intValue());
        } catch (NullPointerException ignored){}
        try {
            searchMap.put("upperBed", upperBedRange.getValue().intValue());
        } catch (NullPointerException ignored){}


        searchMap.put("city", cityFilter.getValue());
        searchMap.put("province", provinceFilter.getValue());
        searchMap.put("postal", postalFilter.getValue());
        searchMap.put("country", countryFilter.getValue());
        searchMap.put("pets_allowed", petsAllowedComboBox.getValue());
    }

    private void showGridEditDialog(ItemClickEvent itemClickEvent) {
        Notification.show(itemClickEvent.getItem().toString());
        viewPropertyDialog = new LandlordViewPropertyDialog((Property) itemClickEvent.getItem());
        viewPropertyDialog.open();
    }

    private void renderSubscriptionButtons(){
        subscribeButton = new Button("Subscribe");
        subscribeButton.setWidth("130px");
        subscribeButton.addClickListener(subEvent->{
            Notification.show("SUBED");
            buildSearchMap();

            //            TODO add sub / use map
        });
        unsubscribeButton = new Button("Unsubscribe");
        unsubscribeButton.setWidth("130px");
        unsubscribeButton.addClickListener(subEvent->{
            Notification.show("UNSUBED");

            //            TODO rm sub
        });
        subscribeButton.setVisible(false);
        unsubscribeButton.setVisible(false);
        if(isRegistered) {
            if(registered_renter.getSubscription() == null) {
                subscribeButton.setVisible(true);
            } else {
                unsubscribeButton.setVisible(true);
            }
        }
    }
}
