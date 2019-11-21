package com.RPMS.view.landlord;

import com.RPMS.model.entity.Property;
import com.RPMS.view.helpers.AddressFieldComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.theme.lumo.Lumo;

public class LandlordAddPropertyDialog extends Dialog {

    private FormLayout formLayout;
    private Property property = new Property();
    private Button saveButton;
    private Button closeButton;
    private HorizontalLayout bottomBar;
    private Binder<Property> binder = new Binder<>(Property.class);
    private AddressFieldComponent addressField;

    public LandlordAddPropertyDialog() {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setHeight("750px");
        setWidth("1000px");
        VerticalLayout layout = new VerticalLayout();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        layout.setDefaultHorizontalComponentAlignment(
                FlexComponent.Alignment.STRETCH);

        //        Bottom buttons
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

    /**
     * Form layout
     */
    private void createForm() {
//    public Property(double price, int beds, int bathrooms, List<Amenity > amenities, Property.Pets_Allowed
//        petsAllowed, Landlord landlord, Contract contract, Address address, Date dateAdded, Property.Property_Status
//        propertyStatus) {

        formLayout = new FormLayout();

        addressField = new AddressFieldComponent();

        addressField.setRequiredIndicatorVisible(true);

        binder.bind(addressField, Property::getAddress,
                Property::setAddress);

        // Updates the value in each bound field component
        binder.readBean(property);


        formLayout.add(addressField);

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
