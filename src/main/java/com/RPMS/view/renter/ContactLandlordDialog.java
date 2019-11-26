package com.RPMS.view.renter;

import com.RPMS.controller.LoginController;
import com.RPMS.controller.contact_strategy.ContactController;
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

public class ContactLandlordDialog extends Dialog {
    private Property property;
    private Button sendButton;
    private Button closeButton;
    private HorizontalLayout bottomBar;
    private VerticalLayout panel;
    private TextField emailField;
    private RichTextEditor message;
    private ContactController contactController;
    private LoginController loginController;


    public ContactLandlordDialog(Property property) {
        contactController = ContactController.getInstance();
        this.property = property;
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setHeight("600px");
        setWidth("750px");
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
        sendButton = new Button("Send");
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
            sendButton.setEnabled(false);
            String emailAddress = emailField.getValue();
            String emailMessage = message.getHtmlValue();
            if(!emailAddress.equals("")){
                //send email to landlord
                contactController.performContactLandlord(emailAddress, emailMessage, property);
                close();
            }
            else{
                //if no email was entered in the field
                Notification.show("Please Enter an Email Address...");
                sendButton.setEnabled(true);
            }
        } catch (Exception e) {
            sendButton.setEnabled(true);
            e.printStackTrace();
        }
    }

    private void fillPanel() {
        emailField = new TextField("Your Email");
        loginController = LoginController.getInstance();
        if(loginController.isLoggedIn()){
            //auto fill email field if user has an account email
            emailField.setValue(LoginController.getInstance().getUserEmail());
        }
        message = new RichTextEditor();
        message.setHeight("300px");
        String messageVal = "[{\"insert\":\"Hello,\\nI am interested in the property at \"},{\"attributes\":{\"italic\":true},\"insert\":\""+property.getAddress().toString()+".\"},{\"insert\":\"\\nThanks\\n\"}]";
        message.setValue(messageVal);

        panel.add(emailField, message);
    }
}
