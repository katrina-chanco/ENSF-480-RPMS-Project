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
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.Lumo;


//@PageTitle("register")
//@Route(value = "register")
public class RegistrationView extends Dialog {
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
    private HorizontalLayout layout;
    private VerticalLayout header;
    private AddressFieldComponent addressField;
    private EmailFieldComponent emailField;
    private NameFieldComponent nameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private ComboBox<String> accountTypeComboBox;

    /**
     * Instantiates registration form
     */
    public RegistrationView() {
        header = new VerticalLayout();
        layout = new HorizontalLayout();
        setHeight("510px");
        setWidth("760px");
        H2 h2 = new H2( "Register");
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);
        layout.setWidth("75%");
        header.add(h2);
        header.setMargin(false);
        header.setPadding(false);
        registrationForm();
        add(header);
        add(layout);
    }

    /**
     * Creates a registration form and adds it to the layour
     */
    private void registrationForm() {
        addressField = new AddressFieldComponent();
        nameField = new NameFieldComponent();
        emailField = new EmailFieldComponent();
        passwordField = new PasswordField();
        confirmPasswordField = new PasswordField();

        passwordField.setLabel("Password");
        confirmPasswordField.setLabel("Confirm password");

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

        initializeAccountTypeComboBox();

        /**
         * Layputs and components
         */
        Button closeButton = new Button("Close");
        closeButton.addClickListener(e -> close());
        VerticalLayout passwordLayout = new VerticalLayout(passwordField, confirmPasswordField);
        passwordLayout.setMargin(false);
        passwordLayout.setPadding(false);
        VerticalLayout verticalLayout1 = new VerticalLayout(nameField, new HorizontalLayout(passwordLayout));
        VerticalLayout verticalLayout2 = new VerticalLayout(addressField, emailField, accountTypeComboBox, new HorizontalLayout(), new HorizontalLayout(new HorizontalLayout(), confirmButton(), closeButton));
        verticalLayout1.setPadding(false);
        verticalLayout2.setPadding(false);
        layout.add(verticalLayout1, verticalLayout2);
        accountBinder.forField(passwordField);
        accountBinder.forField(confirmPasswordField);
    }

    private void initializeAccountTypeComboBox() {
        accountTypeComboBox = new ComboBox<>("Account Type");
        accountTypeComboBox.setItems("Landlord", "Renter");
    }

    /**
     * Checks if text fields are validated and passwords match
     *
     * @throws Exception if fields not validates
     */
    public void isValid() throws Exception {
        if(passwordField.getValue().equals(confirmPasswordField.getValue())) {
            passwordsMatch = true;
        }
        if (emailField.isEmpty() || passwordField.isEmpty() || addressField.isEmpty() || nameField.isEmpty() || !passwordsMatch || accountTypeComboBox.isEmpty()) {
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
            // create an account based on combobox selection
            try {
                switch (accountTypeComboBox.getValue()) {
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
                accountBinder.writeBean(account);
                isValid();
                LoginController.getInstance().loginRegistrationAccount(account);
                confirmButton.setEnabled(false);
                getUI().ifPresent(ui -> ui.navigate(HomePageView.class));
                this.close();
                Notification.show("Registration success!", 3000, Notification.Position.TOP_START);
            } catch (Exception v) {
                if (accountTypeComboBox.isEmpty()) {
                    Notification.show("Please select an account type");
                } else if (!passwordsMatch) {
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
