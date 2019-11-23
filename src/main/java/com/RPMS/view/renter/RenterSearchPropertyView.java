package com.RPMS.view.renter;

import com.RPMS.controller.LoginController;
import com.RPMS.model.entity.Property;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RenterSearchPropertyView extends Dialog {
    private boolean isRegistered;
    private HorizontalLayout rootView;
    private VerticalLayout searchBox;
    private Grid<Property> propertyGrid;
    RenterSearchPropertyView() {
        isRegistered = LoginController.getInstance().isLoggedIn() && !LoginController.getInstance().isLoggedInUnregisteredRenter();
        HorizontalLayout rootView = new HorizontalLayout();
        VerticalLayout searchBox = new VerticalLayout();


    }
}
