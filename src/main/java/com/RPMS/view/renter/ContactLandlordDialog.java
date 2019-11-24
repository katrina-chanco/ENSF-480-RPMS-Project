package com.RPMS.view.renter;

import com.RPMS.model.entity.Property;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
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


    public ContactLandlordDialog(Property property) {
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
//            EMAIL
            close();
        } catch (Exception e) {
            sendButton.setEnabled(true);
            e.printStackTrace();
        }
    }

    private void fillPanel() {
        emailField = new TextField("Email");
//        TODO populate email
//        emailField.setValue(LoginController.getInstance().g);

        message = new RichTextEditor();
        message.setHeight("300px");
        String messageVal = "[{\"insert\":\"Hello,\\nI am interested in the property at \"},{\"attributes\":{\"italic\":true},\"insert\":\""+property.getAddress().toString()+".\"},{\"insert\":\"\\nThanks\\n\"}]";
        message.setValue(messageVal);

        panel.add(emailField, message);
    }
}
