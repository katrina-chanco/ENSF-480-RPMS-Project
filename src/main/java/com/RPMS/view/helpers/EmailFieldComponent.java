package com.RPMS.view.helpers;

import com.RPMS.model.entity.Email;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class EmailFieldComponent extends CustomField<Email> {
    private TextField emailField;

    public EmailFieldComponent() {
        emailField = new TextField();
        emailField.setLabel("Email");
        emailField.setWidth("320px");
        emailField.setRequired(true);
        add(new HorizontalLayout(emailField));
    }

    public void setEnabled(Boolean status) {
        emailField.setEnabled(status);
    }

    @Override
    protected Email generateModelValue() {
        Email email = new Email();
        email.setEmailAddress(emailField.getValue());
        return email;
    }

    @Override
    protected void setPresentationValue(Email email) {
        emailField.setValue(email.getEmailAddress());
    }

}
