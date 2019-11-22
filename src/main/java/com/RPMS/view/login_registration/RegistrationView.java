package com.RPMS.view.login_registration;

import com.RPMS.controller.LoginController;
import com.RPMS.model.entity.Account;
import com.RPMS.model.entity.Landlord;
import com.RPMS.model.entity.Registered_Renter;
import com.RPMS.view.HomePageView;
import com.RPMS.view.helpers.AddressFieldComponent;
import com.RPMS.view.helpers.EmailFieldComponent;
import com.RPMS.view.helpers.NameFieldComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;


@PageTitle("register")
@Route(value = "register")
public class RegistrationView extends Div {
    /**
     * Account of user registering
     */
    private Account account;
    /**
     * Binder to account class
     */
    private Binder<Account> accountBinder = new Binder<>(Account.class);
    /**
     * Flag to check if password was confirmed asuccessfully
     */
    private boolean passwordsMatch;
    /**
     * Layputs and components
     */
    private HorizontalLayout layout;
    private VerticalLayout header;
    private AddressFieldComponent addressField;
    private EmailFieldComponent emailField;
    private NameFieldComponent nameField;
    private PasswordField passwordField;
    private H4 h4;
    private PasswordField confirmPasswordField;
    private ComboBox<String> accountTypeComboBox;

    /**
     * Instantiates registration form
     */
    public RegistrationView() {
        header = new VerticalLayout();
        layout = new HorizontalLayout();

        H3 h3 = new H3("Registration");
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);
        layout.setAlignSelf(FlexComponent.Alignment.CENTER);
        layout.setWidth("75%");
        header.add(h3);
        registrationForm();
        add(header);
        add(layout);
    }

    /**
     * Creates a registration form and adds it to the layour
     */
    private void registrationForm() {
        h4 = new H4();
        h4.setText("Select an account type to enable the fields");

        header.add(new HorizontalLayout(h4));
        addAccountTypeComboBox();

        addressField = new AddressFieldComponent();
        nameField = new NameFieldComponent();
        emailField = new EmailFieldComponent();
        passwordField = new PasswordField();
        confirmPasswordField = new PasswordField();

        passwordField.setLabel("Password");
        confirmPasswordField.setLabel("Confirm password");

        if (LoginController.getInstance().getRegistrationSelectedType() == null) {
            setTextFieldsEnabled(false);
        }

        addressField.setRequiredIndicatorVisible(true);
        nameField.setRequiredIndicatorVisible(true);
        emailField.setRequiredIndicatorVisible(true);
        passwordField.setRequiredIndicatorVisible(true);
        confirmPasswordField.setRequiredIndicatorVisible(true);

        accountBinder.bind(addressField, Account::getAddress, Account::setAddress);
        accountBinder.bind(nameField, Account::getName, Account::setName);
        accountBinder.bind(emailField, Account::getEmail, Account::setEmail);
        accountBinder.forField(passwordField)
                .withValidator(password -> password.equals(confirmPasswordField.getValue()), "Passwords don't match")
                .withValidationStatusHandler(status -> passwordsMatch = false)
                .bind(Account::getPassword, Account::setPassword);

        VerticalLayout verticalLayout1 = new VerticalLayout(nameField, new HorizontalLayout(passwordField), new HorizontalLayout(confirmPasswordField));
        VerticalLayout verticalLayout2 = new VerticalLayout(addressField, emailField, confirmButton());
        verticalLayout2.setSpacing(true);

        layout.add(verticalLayout1, verticalLayout2);
        accountBinder.forField(passwordField);
        accountBinder.forField(confirmPasswordField);
    }

    /**
     * Sets textfields to be enabled or disabled
     *
     * @param status
     */
    private void setTextFieldsEnabled(Boolean status) {
        addressField.setEnabled(status);
        nameField.setEnabled(status);
        emailField.setEnabled(status);
        passwordField.setEnabled(status);
        confirmPasswordField.setEnabled(status);
    }

    private void addAccountTypeComboBox() {
        accountTypeComboBox = new ComboBox<>("Account Type");
        accountTypeComboBox.setItems("Landlord", "Renter");
        accountTypeComboBox.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                accountTypeComboBox.setEnabled(true);
            } else {
                LoginController.getInstance().setRegistrationSelectedType(event.getValue());
                accountTypeComboBox.setEnabled(false);
                setTextFieldsEnabled(true);
                h4.setText(LoginController.getInstance().getRegistrationSelectedType() + " Registration");
                // create an account based on combobox selection
                switch (LoginController.getInstance().getRegistrationSelectedType()) {
                    case "Landlord":
                        account = new Landlord();
                        break;
                    case "Renter":
                        account = new Registered_Renter();
                        break;
                    default:
                        System.out.println("ERROR in selecting account type");
                        break;
                }
            }
        });
        HorizontalLayout hl = new HorizontalLayout();
        hl.add(new HorizontalLayout(accountTypeComboBox));
        header.add(hl);
    }

    /**
     * Checks if text fields are validated and passwords match
     *
     * @throws Exception if fields not validates
     */
    public void isValid() throws Exception {
        passwordField.setErrorMessage("dfjnklms,cdms,");
        if(passwordField.getValue().equals(confirmPasswordField.getValue())) {
            passwordsMatch = true;
        }
        if(emailField.isInvalid() || passwordField.isInvalid() || addressField.isInvalid() || nameField.isInvalid() || !passwordsMatch){
            throw new Exception();
        }
    }

    /**
     * Creates a button to confrim account creation
     * @return
     */
    private Button confirmButton() {
        Button confirmButton = new Button("Confirm");
        confirmButton.setThemeName(Lumo.DARK);
        confirmButton.addClickListener(e -> {
            try {
                accountBinder.writeBean(account);
                isValid();
                LoginController.getInstance().loginRegistrationAccount(account);
                confirmButton.setEnabled(false);
                getUI().ifPresent(ui -> ui.navigate(HomePageView.class));
            } catch (Exception v) {
                if (!passwordsMatch) {
                    Notification.show("Passwords don't match");
                } else {
                    Notification.show("Please enter information in all the fields.");
                }
                confirmButton.setEnabled(true);
            }
        });
        return confirmButton;
    }

}
