package com.RPMS.view.landlord;

import com.RPMS.model.entity.Property;
import com.RPMS.view.helpers.GridHelpers;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.text.NumberFormat;

public class LandlordViewPropertyDialog extends Dialog {
    private Property property;
    private VerticalLayout panel;
    private Button closeButton;
    private HorizontalLayout bottomBar;
    private Dialog changeStatusDialog;
    private Button changeStatusButton;
    private Button editPropertyButton;
    private Dialog editPropertyDialog;


    /**
     * Configure the UI
     */
    public LandlordViewPropertyDialog(Property property) {
        this.property = property;

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setHeight("750px");
        setWidth("605px");

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

//        Panel
        panel = new VerticalLayout();
        panel.setMinHeight("675px");
        panel.setWidth("100%");

        bottomBar.add(panel, closeButton);
        fillPropertyInfo();

        layout.add(panel, bottomBar);
        add(layout);
    }

    /**
     * Builds the UI
     */
    private void fillPropertyInfo() {
//        ELEMENTS OF DIALOG
        H3 pAddress = new H3(property.getAddress().toString());

        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        H4 pPrice = new H4(defaultFormat.format(property.getPrice()) + "/month");

        Component pBeds = GridHelpers.getBeds().createComponent(property);
        Component pBath = GridHelpers.getBathrooms().createComponent(property);
        Component contract = GridHelpers.getPropertyContract().createComponent(property);
        Component petStatus = GridHelpers.getPropertyPetBadge().createComponent(property);
        Component pStatus = GridHelpers.getPropertyStatusBadge().createComponent(property);
        Component imageLayout = GridHelpers.getImageList().createComponent(property);
//        CHANGE STATUS
        changeStatusButton = new Button("Update Property Status");
        changeStatusButton.addClickListener(updateEvent -> {
                    changeStatusDialog = new LandlordChangeStatusDialog(property);
                    changeStatusDialog.open();
                    changeStatusDialog.addOpenedChangeListener(e -> {
                        close();
                    });
                }
        );

//        EDIT PROPERTY
        editPropertyButton = new Button("Edit");
        editPropertyButton.addClickListener(editEvent->{
            editPropertyDialog = new LandlordAddEditPropertyDialog(property);
            editPropertyDialog.open();
            editPropertyDialog.addOpenedChangeListener(e->{
                close();
            });
        });

        panel.add(
                pAddress,
                pPrice,
                new HorizontalLayout(
                        pBeds, pBath, contract, petStatus, pStatus
                ),
                imageLayout,
                new HorizontalLayout(changeStatusButton, editPropertyButton)
        );
    }
}
