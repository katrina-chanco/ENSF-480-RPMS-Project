package com.RPMS.view.landlord;

import com.RPMS.model.Amenities;
import com.RPMS.model.entity.Property;
import com.RPMS.view.helpers.AddressFieldComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.theme.lumo.Lumo;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.*;

public class LandlordAddPropertyDialog extends Dialog {
    private FormLayout formLayout;
    private Property property = new Property();
    private Button saveButton;
    private Button closeButton;
    private HorizontalLayout bottomBar;
    private Binder<Property> binder = new Binder<>(Property.class);

    private AddressFieldComponent addressField;
    private NumberField propertyPriceField;
    private NumberField propertyBedField;
    private NumberField propertyBathField;
    private ComboBox<Property.Pets_Allowed> propertyPetStatus;
    private MultiselectComboBox<Amenities> amenitiesField;

    public LandlordAddPropertyDialog() {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setHeight("750px");
        setWidth("1000px");
        VerticalLayout layout = new VerticalLayout();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        layout.setDefaultHorizontalComponentAlignment(
                FlexComponent.Alignment.STRETCH);

        //  Bottom buttons
        bottomBar = new HorizontalLayout();
        bottomBar.setHeight("50px");
        bottomBar.setWidth("100%");
        closeButton = new Button("Close");
        closeButton.addClickListener(e -> close());
        saveButton = new Button("Save");
        saveButton.setThemeName(Lumo.DARK);
        saveButton.addClickListener(e -> saveButton());

        Button testB = new Button("Test");
        testB.addClickListener(e->{
            System.out.println(property.getAddress().toString());
            Notification.show("Stuff");
        });
        bottomBar.add(testB);

        bottomBar.add(closeButton, saveButton);
        createForm();

        formLayout.setHeight("675px");
        formLayout.setWidth("100%");

        layout.add(formLayout, bottomBar);
        add(layout);
    }

    private void generateProperty(){
        property = new Property();
        property.setDateAdded(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        property.setContract(null);
//        property.setLandlord(
//        pr);
        property.setPropertyStatus(Property.Property_Status.SUSPENDED);
    }

    /**
     * Form layout
     */
    private void createForm() {
        formLayout = new FormLayout();

//        FIELDS
        addressField = new AddressFieldComponent();
        addressField.setRequiredIndicatorVisible(true);

        propertyPriceField = new NumberField("Cost/Month");
        propertyPriceField.setWidth("150px");

        propertyBedField = new NumberField("Beds");
        propertyBedField.setWidth("100px");
        propertyBedField.setMax(10);
        propertyBedField.setMin(1);
        propertyBedField.setHasControls(true);

        propertyBathField = new NumberField("Baths");
        propertyBathField.setWidth("100px");
        propertyBathField.setMax(10);
        propertyBathField.setMin(1);
        propertyBathField.setHasControls(true);

        propertyPetStatus = new ComboBox<>("Pets Allowed");
        propertyPetStatus.setWidth("133px");

        List<Property.Pets_Allowed> pets_allowedList = new ArrayList<>(EnumSet.allOf(Property.Pets_Allowed.class));

        propertyPetStatus.setDataProvider(new ListDataProvider<>(pets_allowedList));
        propertyPetStatus.setItemLabelGenerator(Property.Pets_Allowed::getPrettyName);

        amenitiesField = new MultiselectComboBox();

//        // create and add items
        List<Amenities> amenitiesList = new ArrayList<>(EnumSet.allOf(Amenities.class));
        amenitiesField.setDataProvider(new ListDataProvider<>(amenitiesList));
        amenitiesField.setItemLabelGenerator(Amenities::getAmenityName);
        amenitiesField.setWidth("530px");
//        BINDERS
        binder.bind(addressField, Property::getAddress,
                Property::setAddress);

        // Updates the value in each bound field component
        binder.readBean(property);

        formLayout.add(new VerticalLayout(
                addressField,
                new HorizontalLayout(propertyPriceField, propertyBedField, propertyBathField, propertyPetStatus),
                amenitiesField
            )
        );

    }

    /**
     * update bean
     */
    private void saveButton() {
        try {
            binder.writeBean(property);
            // A real application would also save
            // the updated person
            // using the application's backend
        } catch (ValidationException e) {
            Notification.show("Please check all the fields.");
        }
    }


}
