package com.RPMS.view.landlord;

import com.RPMS.controller.LoginController;
import com.RPMS.model.entity.Property;
import com.RPMS.view.helpers.GridHelpers;
import com.RPMS.view.renter.ContactLandlordDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
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
    private Button contactLandlordButton;
    private Dialog editPropertyDialog;
    private Dialog contactLandlordDialog;
    private boolean isPowerUser;
    private boolean isRenter;
    private boolean isUnregisteredRenter;

    /**
     * Configure the UI
     */
    public LandlordViewPropertyDialog(Property property) {
        this.property = property;
        if (LoginController.getInstance().isLoggedInUnregisteredRenter()) {
            isRenter = false;
            isUnregisteredRenter = true;
            isPowerUser = false;
        } else if (LoginController.getInstance().isAccountRenter()) {
            isRenter = true;
            isUnregisteredRenter = false;
            isPowerUser = false;
        } else if (LoginController.getInstance().isAccountLandlord() || LoginController.getInstance().isAccountManager()) {
            isPowerUser = true;
            isUnregisteredRenter = false;
            isRenter = false;
        }
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
        buildShareUI();
        if (isPowerUser) {
            fillPropertyInfoPowerUser();
        } else if (isRenter || isUnregisteredRenter) {
            fillPropertyInfoRenter();
        }

        layout.add(panel, bottomBar);
        add(layout);
    }

    /**
     * Builds the UI
     */
    private void buildShareUI() {
        //        ELEMENTS OF DIALOG
        H3 pAddress = new H3(property.getAddress().toString());

        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        H4 pPrice = new H4(defaultFormat.format(property.getPrice()) + "/month");

        Component pBeds = GridHelpers.getBeds().createComponent(property);
        Component pBath = GridHelpers.getBathrooms().createComponent(property);
        Component contract = isPowerUser ? GridHelpers.getPropertyContract().createComponent(property) : new Label();
        Component petStatus = GridHelpers.getPropertyPetBadge().createComponent(property);
        Component pStatus = GridHelpers.getPropertyStatusBadge().createComponent(property);
        Component imageLayout = GridHelpers.getImageList().createComponent(property);
        panel.add(
                pAddress,
                pPrice,
                new HorizontalLayout(
                        pBeds, pBath, contract, petStatus, pStatus
                ),
                imageLayout
        );
    }

    private void fillPropertyInfoRenter() {
        contactLandlordButton = new Button("Contact Landlord");
        contactLandlordButton.addClickListener(updateEvent -> {
            contactLandlordDialog = new ContactLandlordDialog(property);
            contactLandlordDialog.open();
        });

            panel.add(contactLandlordButton);
        }

        private void fillPropertyInfoPowerUser () {
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
            editPropertyButton.addClickListener(editEvent -> {
                editPropertyDialog = new LandlordAddEditPropertyDialog(property);
                editPropertyDialog.open();
                editPropertyDialog.addOpenedChangeListener(e -> {

                    close();
                });
            });

            panel.add(
                    new HorizontalLayout(changeStatusButton, editPropertyButton)
            );
        }
    }
