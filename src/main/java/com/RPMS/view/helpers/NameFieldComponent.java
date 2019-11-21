package com.RPMS.view.helpers;

import com.RPMS.model.entity.Name;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import static com.vaadin.flow.component.textfield.Autocapitalize.WORDS;

public class NameFieldComponent extends CustomField<Name> {
    private TextField fnameField;
    private TextField mnameField;
    private TextField lnameField;

    public NameFieldComponent() {
        setLabel("Name");
        fnameField = new TextField();
        fnameField.setLabel("First name");
        fnameField.setAutocapitalize(WORDS);
        fnameField.setWidth("200px");
        fnameField.setRequired(true);

        mnameField = new TextField();
        mnameField.setLabel("Middle name");
        mnameField.setAutocapitalize(WORDS);
        mnameField.setWidth("200px");
        mnameField.setRequired(true);

        lnameField = new TextField();
        lnameField.setLabel("Last name");
        lnameField.setAutocapitalize(WORDS);
        lnameField.setWidth("200px");
        lnameField.setRequired(true);

        add(new HorizontalLayout(fnameField), new HorizontalLayout(mnameField), new HorizontalLayout(lnameField));
    }

    public void setEnabled(Boolean status) {
        lnameField.setEnabled(status);
        fnameField.setEnabled(status);
        mnameField.setEnabled(status);
    }

    @Override
    protected Name generateModelValue() {
        Name name = new Name();
        name.setfName(fnameField.getValue());
        name.setlName(lnameField.getValue());
        name.setmName(mnameField.getValue());
        return name;
    }

    @Override
    protected void setPresentationValue(Name name) {
        fnameField.setValue(name.getfName());
        lnameField.setValue(name.getlName());
        mnameField.setValue(name.getmName());
    }
}
