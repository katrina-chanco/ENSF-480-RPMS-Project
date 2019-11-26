package com.RPMS.view.manager;

import com.RPMS.controller.PropertyController;
import com.RPMS.model.entity.Property;
import com.RPMS.view.helpers.GridHelpers;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.theme.lumo.Lumo;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ChangeStatusDialog extends Dialog {
    private Property property;
    private VerticalLayout panel;
    private Button closeButton;
    private HorizontalLayout bottomBar;
    private Button saveButton;
    private ComboBox<Property.Property_Status> property_statusComboBox;
    private NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

    public ChangeStatusDialog(Property property) {
        this.property = property;

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setMinHeight("150px");
        setWidth("350px");

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

        //        Panel
        panel = new VerticalLayout();
        panel.setMinHeight("100px");
        panel.setWidth("100%");

        bottomBar.add(panel, closeButton, saveButton);
        updateStatusView();

        layout.add(panel, bottomBar);
        add(layout);
    }

    private void updateStatusView() {
        Label currentStatus = new Label("Current Status: ");
        Component pStatus = GridHelpers.getPropertyStatusBadge().createComponent(property);
        HorizontalLayout status = new HorizontalLayout(currentStatus, pStatus);

        property_statusComboBox = new ComboBox<>();
        List<Property.Property_Status> statusList = new ArrayList<>(EnumSet.allOf(Property.Property_Status.class));
        property_statusComboBox.setDataProvider(new ListDataProvider<>(statusList));
        property_statusComboBox.setItemLabelGenerator(Property.Property_Status::getPrettyName);
        property_statusComboBox.setValue(property.getPropertyStatus());
        property_statusComboBox.setRequired(true);

        panel.add(status, property_statusComboBox);
    }

    /**
     * update bean
     */
    private void saveButton() {
        try {
            saveButton.setEnabled(false);
            PropertyController.getInstance().updateStatus(property, property_statusComboBox.getValue());
            close();
        } catch (Exception e) {
            saveButton.setEnabled(true);
            e.printStackTrace();
        }
    }


}


