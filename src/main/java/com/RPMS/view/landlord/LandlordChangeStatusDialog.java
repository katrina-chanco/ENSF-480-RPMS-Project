package com.RPMS.view.landlord;

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

public class LandlordChangeStatusDialog extends Dialog {
    private Property property;
    private VerticalLayout panel;
    private Button closeButton;
    private HorizontalLayout bottomBar;
    private Button saveButton;
    private Double subCost;
    private Integer subDays;
    private NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
    private PaymentRequest paymentRequest;
    private Button payButton;

    public LandlordChangeStatusDialog(Property property) {
        this.property = property;

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setHeight("320px");
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

//        Panel
        panel = new VerticalLayout();
        panel.setMinHeight("200px");
        panel.setWidth("100%");

        bottomBar.add(panel, closeButton, saveButton);
        addPayment();
        updateStatusView();

        layout.add(panel, bottomBar);
        add(layout);
    }

    private void updateStatusView() {
        Label currentStatus = new Label("Current Status: ");
        Component pStatus = GridHelpers.getPropertyStatusBadge().createComponent(property);
        HorizontalLayout status = new HorizontalLayout(currentStatus, pStatus);
        Label infoLabel = new Label();

        ComboBox<Property.Property_Status> property_statusComboBox = new ComboBox<>();
        List<Property.Property_Status> statusList = new ArrayList<>(EnumSet.allOf(Property.Property_Status.class));
        property_statusComboBox.setDataProvider(new ListDataProvider<>(statusList));
        property_statusComboBox.setItemLabelGenerator(Property.Property_Status::getPrettyName);

        infoLabel.getStyle().set("color", "red");
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
                        .show("Payment collection is not supported on your browser!", 9000, Notification.Position.MIDDLE));
            }
        });
        payButton = new Button("Pay");
        panel.add(payButton);
    }

    //    PAYMENT API FROM: https://vaadin.com/directory/component/payment-request-addon/l
    private void addPaymentRequestHandlerToButton() {
        JsonArray supportedPaymentMethods = getSupportedMethods();

        JsonObject paymentDetails = getPaymentDetails();

        PaymentRequest paymentRequest = new PaymentRequest(supportedPaymentMethods, paymentDetails);
        paymentRequest.setPaymentResponseCallback((paymentResponse) -> {
            JsonObject eventData = paymentResponse.getEventData();
            Notification.show("Please wait a moment while we finish the payment via our payment gateway.", 9000,
                    Notification.Position.MIDDLE);

            Command onPaymentGatewayRequestComplete = () -> {
                // Close the Payment Request native dialog
                paymentResponse.complete();
                String cardNumber = eventData.getObject("details").getString("cardNumber");
                String cardEnding = cardNumber.substring(cardNumber.length() - 4);
                Notification
                        .show("Purchase complete! We have charged the total (1337€) from your credit card ending in "
                                + cardEnding, 9000, Notification.Position.MIDDLE);
            };
            startPaymentGatewayQuery(paymentResponse, eventData, onPaymentGatewayRequestComplete);
        });
        paymentRequest.install(payButton);

    }

    /**
     * simulates asynchronous communication with a payment gateway
     *
     * @param paymentResponse
     * @param eventData
     * @param onPaymentGatewayRequestComplete
     */
    private void startPaymentGatewayQuery(PaymentRequest.PaymentResponse paymentResponse, JsonObject eventData,
                                          Command onPaymentGatewayRequestComplete) {
        UI ui = UI.getCurrent();
        Thread paymentGatewayThread = new Thread(() -> {
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ui.access(onPaymentGatewayRequestComplete);

        });
        paymentGatewayThread.start();
    }

    /**
     * @return <code>[{supportedMethods: 'basic-card'}]</code>
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
     * @return <code>total: { label: 'Cart (10 items)', amount:{ currency: 'EUR', value:
     * 1337 } }</code>
     */
    private JsonObject getPaymentDetails() {
        JreJsonFactory jsonFactory = new JreJsonFactory();
        JsonObject paymentDetails = jsonFactory.createObject();

        JsonObject total = jsonFactory.createObject();
        total.put("label", "Cart (10 items)");
        JsonObject totalAmount = jsonFactory.createObject();
        totalAmount.put("currency", "EUR");
        totalAmount.put("value", "1337");
        total.put("amount", totalAmount);
        paymentDetails.put("total", total);
        return paymentDetails;
    }
}
