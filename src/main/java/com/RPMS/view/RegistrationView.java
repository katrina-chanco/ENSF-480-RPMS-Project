package com.RPMS.view;

import com.RPMS.model.entity.Account;
import com.RPMS.model.entity.Address;
import com.RPMS.model.entity.Email;
import com.RPMS.model.entity.Name;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;


@PageTitle("register")
@Route(value = "register")
public class RegistrationView extends Div {

    public RegistrationView() {
        VerticalLayout vl = new VerticalLayout();
        H3 head = new H3("Registration");
        vl.add(head, registrationForm());
        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl.setAlignItems(FlexComponent.Alignment.CENTER);
        vl.setAlignSelf(FlexComponent.Alignment.CENTER);
        vl.setWidth("100%");
        add(vl);
    }

    private Component registrationForm() {
        FormLayout form = new FormLayout();

// Binders for form
        Binder<Account> accountBinder = new Binder<Account>(Account.class);
        Binder<Name> nameBinder = new Binder<Name>(Name.class);
        Binder<Email> emailBinder = new Binder<Email>(Email.class);
        Binder<Address> addressBinder = new Binder<Address>(Address.class);
// NAME
// First name
        Label fnameStatus = new Label("First name");
        TextField firstNameField = new TextField();
        nameBinder.forField(firstNameField)
                // define the validator
                .withValidator(fname -> fname.length() >= 3, "First name must contain at least three characters")
                // define how the validation status is displayed
                .withValidationStatusHandler(status -> {
                    fnameStatus.setText(status.getMessage().orElse(""));
                    fnameStatus.setVisible(status.isError());
                })
                .bind(Name::getfName, Name::setfName);
        form.addFormItem(firstNameField, fnameStatus);
// Last name
        Label lnameStatus = new Label("Last name");
        TextField lastNameField = new TextField();
        nameBinder.forField(lastNameField)
                // define the validator
                .withValidator(lname -> lname.length() >= 2, "Last name must contain at least two characters")
                // define how the validation status is displayed
                .withValidationStatusHandler(status -> {
                    lnameStatus.setText(status.getMessage().orElse(""));
                    lnameStatus.setVisible(status.isError());
                })
                .bind(Name::getlName, Name::setlName);
        form.addFormItem(lastNameField, lnameStatus);
// ADDRESS
// House number
        Label houseNoStatus = new Label();
        TextField houseNoField = new TextField();
        addressBinder.forField(houseNoField)
                // define the validator
                .withValidator(houseNo -> houseNo.length() != 0, "House number must contain at least one digit")
                // define how the validation status is displayed
                .withValidationStatusHandler(status -> {
                    houseNoStatus.setText(status.getMessage().orElse(""));
                    houseNoStatus.setVisible(status.isError());
                })
                .withConverter(new StringToIntegerConverter("Must enter a number"))
                .bind(Address::getHouseNo, Address::setHouseNo);
        form.addFormItem(houseNoField, "House number");
        form.addFormItem(houseNoStatus, "");
// Street name
        Label strNameStatus = new Label();
        TextField streetNameField = new TextField();
        addressBinder.forField(streetNameField)
                // define the validator
                .withValidator(streetName -> streetName.length() >= 2, "Street name must contain at least two characters")
                // define how the validation status is displayed
                .withValidationStatusHandler(status -> {
                    houseNoStatus.setText(status.getMessage().orElse(""));
                    houseNoStatus.setVisible(status.isError());
                })
                .bind(Address::getStrName, Address::setStrName);
        form.addFormItem(streetNameField, "Street name");
        form.addFormItem(strNameStatus, "");
// City
        Label cityStatus = new Label();
        TextField cityField = new TextField();
        addressBinder.forField(cityField)
                // define the validator
                .withValidator(city -> city.length() >= 3, "City name must contain at least three characters")
                // define how the validation status is displayed
                .withValidationStatusHandler(status -> {
                    cityStatus.setText(status.getMessage().orElse(""));
                    cityStatus.setVisible(status.isError());
                })
                .bind(Address::getCity, Address::setCity);
        form.addFormItem(cityField, "City name");
        form.addFormItem(cityStatus, "");
// Postal code
        Label postalStatus = new Label();
        TextField postalField = new TextField();
        new CustomStringBlockFormatter(new int[]{3, 3}, new String[]{" "}, CustomStringBlockFormatter.ForceCase.UPPER, null, false).extend(postalField);
        addressBinder.forField(houseNoField)
                // define the validator
                .withValidator(houseNo -> houseNo.length() >= 6, "Postal code must contain six characters")
                // define how the validation status is displayed
                .withValidationStatusHandler(status -> {
                    postalStatus.setText(status.getMessage().orElse(""));
                    postalStatus.setVisible(status.isError());
                })
                .bind(Address::getPostalCode, Address::setPostalCode);
        form.addFormItem(postalField, "Postal code");
        form.addFormItem(postalStatus, "");
// Country
        Label countryStatus = new Label();
        TextField countryField = new TextField();
        addressBinder.forField(cityField)
                // define the validator
                .withValidator(city -> city.length() >= 3, "Country name must contain at least three characters")
                // define how the validation status is displayed
                .withValidationStatusHandler(status -> {
                    countryStatus.setText(status.getMessage().orElse(""));
                    countryStatus.setVisible(status.isError());
                })
                .bind(Address::getCity, Address::setCity);
        form.addFormItem(countryField, "City name");
        form.addFormItem(countryStatus, "");
//
//

//        TextField emailField = new TextField();
//        (emailField, "email");
//        binder.addFormItem(emailField)
//                .withValidator(new EmailOrPhoneValidator())
//                .withValidationStatusHandler(
//                        status -> commonStatusChangeHandler(status,
//                                phoneOrEmailField));
//
//        PasswordField passwordField = new PasswordField();
//        addToLayout(form, passwordField, "Password");
//        passwordBinding = binder.forField(passwordField)
//                .withValidator(new PasswordValidator())
//                .withValidationStatusHandler(
//                        status -> commonStatusChangeHandler(status,
//                                passwordField))
//                .bind(Account::getPassword, Account::setPassword);
//        passwordField.addValueChangeListener(
//                event -> confirmPasswordBinding.validate());
//
//        PasswordField confirmPasswordField = new PasswordField();
//        addToLayout(form, confirmPasswordField, "Password again");
//
//        confirmPasswordBinding = binder.forField(confirmPasswordField)
//                .withValidator(Validator.from(this::validateConfirmPasswd,
//                        "Password doesn't match"))
//                .withValidationStatusHandler(
//                        status -> confirmPasswordStatusChangeHandler(status,
//                                confirmPasswordField))
//                .bind(Account::getPassword, (person, pwd) -> {
//                });
//
//        form.addComponent(createButton());
//
//        firstNameField.focus();
//
//        binder.setBean(new Person());
        return form;
    }


}
