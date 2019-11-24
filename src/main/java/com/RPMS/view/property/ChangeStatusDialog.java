package com.RPMS.view.property;

import com.RPMS.controller.LoginController;
import com.RPMS.controller.PaymentController;
import com.RPMS.controller.PropertyController;
import com.RPMS.controller.SystemSettingController;
import com.RPMS.model.entity.Property;
import com.RPMS.view.helpers.GridHelpers;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.theme.lumo.Lumo;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;
import org.vaadin.jonni.PaymentRequest;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class ChangeStatusDialog extends Dialog {
    private Property property;
    private VerticalLayout panel;
    private Button closeButton;
    private HorizontalLayout bottomBar;
    private Button saveButton;
    private ComboBox<Property.Property_Status> property_statusComboBox;
    private Double subCost;
    private Integer subDays;
    private NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
    private Button payButton;

    public ChangeStatusDialog(Property property) {
        this.property = property;

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setMinHeight("150px");
        setWidth("350px");

        subCost = SystemSettingController.getInstance().getSubscriptionCost();
        subDays = SystemSettingController.getInstance().getSubscriptionLength();

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

        payButton = new Button("Pay Fee & Save");
        payButton.setThemeName(Lumo.DARK);
        payButton.setVisible(false);
        addPayment();

//        Panel
        panel = new VerticalLayout();
        panel.setMinHeight("100px");
        panel.setWidth("100%");

        bottomBar.add(panel, closeButton, saveButton, payButton);
        updateStatusView();

        layout.add(panel, bottomBar);
        add(layout);
    }

    private void updateStatusView() {
        Label currentStatus = new Label("Current Status: ");
        Component pStatus = GridHelpers.getPropertyStatusBadge().createComponent(property);
        HorizontalLayout status = new HorizontalLayout(currentStatus, pStatus);
        Label infoLabel = new Label();

        property_statusComboBox = new ComboBox<>();
        List<Property.Property_Status> statusList = new ArrayList<>(EnumSet.allOf(Property.Property_Status.class));
        property_statusComboBox.setDataProvider(new ListDataProvider<>(statusList));
        property_statusComboBox.setItemLabelGenerator(Property.Property_Status::getPrettyName);
        property_statusComboBox.setValue(property.getPropertyStatus());
        property_statusComboBox.setRequired(true);
        property_statusComboBox.addValueChangeListener(property_statusComboBoxEvent -> {
//            payment needed
            if (isPaymentNeeded(property_statusComboBoxEvent.getValue())) {
                infoLabel.setVisible(true);
                saveButton.setVisible(false);
                payButton.setVisible(true);
            } else if (isPaymentActive(property_statusComboBoxEvent.getValue())) {
                infoLabel.setVisible(true);
            } else {
                infoLabel.setVisible(false);
                saveButton.setVisible(true);
                payButton.setVisible(false);
            }
        });
        infoLabel.getStyle().set("color", "red");
        infoLabel.setVisible(false);
        if (property.getPropertyStatus() == Property.Property_Status.ACTIVE) {
            infoLabel.setText("Changing the status from ACTIVE will invalidate current subscription.");
        } else {
            LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            infoLabel.setText(new StringBuilder().append("Changing the status to ACTIVE will require a subscription of ")
                    .append(defaultFormat.format(subCost)).append(" valid until ")
                    .append(formatter.format(Date.from(localDateTime.plusDays(subDays).atZone(ZoneId.systemDefault()).toInstant())))
                    .append(" (").append(subDays).append(" days)").toString());
        }
        panel.add(status, property_statusComboBox, infoLabel);
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

    private void addPayment() {
        PaymentRequest.queryIsSupported(isSupported -> {
            if (isSupported) {
                addPaymentRequestHandlerToButton();
            } else {
                payButton.addClickListener(click -> Notification
                        .show("Payment collection is not supported on your browser! Please use latest Google Chrome. Known to work. ", 9000, Notification.Position.MIDDLE));
            }
        });
    }

    //    PAYMENT API FROM: https://vaadin.com/directory/component/payment-request-addon/
    private void addPaymentRequestHandlerToButton() {
        JsonArray supportedPaymentMethods = getSupportedMethods();

        JsonObject paymentDetails = getPaymentDetails();

        PaymentRequest paymentRequest = new PaymentRequest(supportedPaymentMethods, paymentDetails);
        paymentRequest.setPaymentResponseCallback((paymentResponse) -> {
            JsonObject eventData = paymentResponse.getEventData();
            Command onPaymentGatewayRequestComplete = () -> {
                // Close the Payment Request native dialog
                paymentResponse.complete();
                String cardNumber = eventData.getObject("details").getString("cardNumber");
                String cardEnding = cardNumber.substring(cardNumber.length() - 4);
                Notification
                        .show("Purchase complete! We have charged the total " + defaultFormat.format(subCost) + " from your credit card ending in "
                                + cardEnding);
                saveButton();
            };
            startPaymentGatewayQuery(eventData, onPaymentGatewayRequestComplete);
        });
        paymentRequest.install(payButton);
    }

    /**
     *  save to DB and 'charge' card
     *
     */
    private void  startPaymentGatewayQuery(JsonObject eventData, Command onPaymentGatewayRequestComplete) {
        UI ui = UI.getCurrent();
        PaymentController.getInstance().addPayment(property, eventData, subCost);
        ui.access(onPaymentGatewayRequestComplete);
    }

    /**
     * allow cards
     */
    private JsonArray getSupportedMethods() {
        JreJsonFactory jsonFactory = new JreJsonFactory();
        JsonArray supportedPaymentMethods = jsonFactory.createArray();
        JsonObject basicCard = jsonFactory.createObject();
        basicCard.put("supportedMethods", "basic-card");
        supportedPaymentMethods.set(0, basicCard);
        return supportedPaymentMethods;
    }

    /**
     * Build payment with amount
     */
    private JsonObject getPaymentDetails() {
        JreJsonFactory jsonFactory = new JreJsonFactory();
        JsonObject paymentDetails = jsonFactory.createObject();
        JsonObject total = jsonFactory.createObject();
        total.put("label", "RPMS: " + property.getAddress().toString() + " subscription.");
        JsonObject totalAmount = jsonFactory.createObject();
        totalAmount.put("currency", "CAD");
        totalAmount.put("value", subCost);
        total.put("amount", totalAmount);
        paymentDetails.put("total", total);
        return paymentDetails;
    }

    /**
     * is payment needed for status change
     *
     * @param property_status potential status
     * @return
     */
    private boolean isPaymentNeeded(Property.Property_Status property_status) {
        return PaymentController.getInstance().isPaymentNeeded() && property.getPropertyStatus() != Property.Property_Status.ACTIVE && property_status == Property.Property_Status.ACTIVE;
    }

    private boolean isPaymentActive(Property.Property_Status property_status) {
        return PaymentController.getInstance().isPaymentNeeded() && property.getPropertyStatus() == Property.Property_Status.ACTIVE && property_status != Property.Property_Status.ACTIVE;
    }
}
