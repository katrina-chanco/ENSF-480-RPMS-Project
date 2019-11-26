package com.RPMS.view.property;

import com.RPMS.controller.ContractController;
import com.RPMS.model.entity.Property;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.Lumo;

public class SendContractDialog extends Dialog {
    private Property property;
    private Button sendButton;
    private Button closeButton;
    private HorizontalLayout bottomBar;
    private VerticalLayout panel;
    private TextField emailField;
    private RichTextEditor message;


    public SendContractDialog(Property property) {
        this.property = property;
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setHeight("200px");
        setWidth("300px");
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
        sendButton = new Button("Send Contract");
        sendButton.setThemeName(Lumo.DARK);
        sendButton.addClickListener(e -> sendButton());

        bottomBar.add(closeButton, sendButton);

//        Panel
        panel = new VerticalLayout();
        panel.setMinHeight("100px");
        panel.setWidth("100%");
        fillPanel();
        layout.add(panel, bottomBar);
        add(layout);
    }

    private void sendButton() {
        try {
            if(emailField.isEmpty()) {
                Notification.show("Please enter a email.");
            } else {
                sendButton.setEnabled(false);
                ContractController.getInstance().sendContract(emailField.getValue(), property);
                close();
            }

        } catch (Exception e) {
            sendButton.setEnabled(true);
            e.printStackTrace();
        }
    }

    private void fillPanel() {
        emailField = new TextField("Email");
        panel.add(emailField);
    }
}
