package com.RPMS.view.manager;

import com.RPMS.MainView;
import com.RPMS.controller.SystemSettingController;
import com.RPMS.model.entity.SystemSetting;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;


@Route(value = "manager/SelectSystemOptions/ModifyPayment", layout = MainView.class)
public class ModifyPaymentView extends Div {

    private NumberField numberField;
    private SystemSetting sysSettingAmount;
    private SystemSetting sysSettingPeriod;
    private Grid<SystemSetting>gridResult = new Grid<>();
    private List<SystemSetting> dataCols = new ArrayList<>();
    private SystemSettingController systemSettingController;


    public ModifyPaymentView(){
        systemSettingController = SystemSettingController.getInstance();
        Label instruction = new Label("Insert New Value and Select Which Field to Update:");
        add(new VerticalLayout(
                new HorizontalLayout(instruction)
        ));

        //Modifying a Numerical Value
        numberField = new NumberField("New Input Numerical Value");
        numberField.setWidth("300px");
        numberField.setHasControls(true);
        numberField.setValue(1d);
        numberField.setMin(0);
        numberField.setMax(1000000000);

        Button bpaymentAmount = new Button("Change Payment Amount");
        Button bpaymentPeriod = new Button("Change Payment Period");

        add(new VerticalLayout(numberField));
        add(new VerticalLayout(
                new HorizontalLayout(bpaymentAmount, bpaymentPeriod),
                gridResult
        ));

        //Updating the Payment Amount
        bpaymentAmount.addClickListener(e-> {
            Notification.show("Editing Payment Amount");
            sysSettingAmount = systemSettingController.getSettingByName("sub_amount");
            double input = numberField.getValue();
            String inputAmount = Double.toString(input);
            systemSettingController.setPaymentAmount(sysSettingAmount, inputAmount);
            Notification.show("Payment Amount Updated...");
            displayPaymentModification();
        });

        //Updating the Payment Period
        bpaymentPeriod.addClickListener(e-> {
            Notification.show("Editing Payment Period");
            sysSettingPeriod = systemSettingController.getSettingByName("sub_days");
            int input = numberField.getValue().intValue();
            String inputPeriod = Integer.toString(input);
            systemSettingController.setPaymentPeriod(sysSettingPeriod, inputPeriod);
            Notification.show("Payment Period Updated...");
            displayPaymentModification();
        });
    }

    //Displaying changes in the Database
    private void displayPaymentModification(){
        systemSettingController = SystemSettingController.getInstance();
        gridResult.removeAllColumns();
        dataCols = new ArrayList<>();

        sysSettingAmount = systemSettingController.getSettingByName("sub_amount");
        sysSettingPeriod = systemSettingController.getSettingByName("sub_days");
        System.out.println(sysSettingAmount.getSettingName() + " " + sysSettingAmount.getSettingValue());
        System.out.println(sysSettingPeriod.getSettingName() + " " + sysSettingPeriod.getSettingValue());

        dataCols.add(sysSettingAmount);
        dataCols.add(sysSettingPeriod);

        gridResult.setItems(dataCols);
        gridResult.addColumn((SystemSetting::getSettingName)).setHeader("Payment Setting Type");
        gridResult.addColumn((SystemSetting::getSettingValue)).setHeader("Payment Setting Value");
    }
}
