package com.RPMS.view;

import com.RPMS.MainView;
import com.RPMS.controller.LoginController;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle("login")
@Route(value = "login")
public class LoginView extends Div {

    public LoginView() {
        VerticalLayout vl = new VerticalLayout();
        H2 head = new H2("Welcome to RPMS");
        vl.add(head, loginForm(), unregisteredRenterButton());
        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl.setAlignItems(FlexComponent.Alignment.CENTER);
        vl.setAlignSelf(FlexComponent.Alignment.CENTER);
        vl.setWidth("100%");
        add(vl);
    }

    private Component loginForm() {
        LoginForm component = new LoginForm();
        addCustomI81n(component);
        checkIfLoggedIn(component);
        addloginListener(component);
        addRegistrationListener(component);
        return component;
    }

    private Button unregisteredRenterButton() {
        Button continue_as_an_unregistered_renter = new Button("Continue as an unregistered renter");
        continue_as_an_unregistered_renter.setThemeName(Lumo.DARK);
        continue_as_an_unregistered_renter.setVisible(true);
        continue_as_an_unregistered_renter.addClickListener(e -> {
            Notification.show("Entering RPMS as an unregistered renter...", 3000, Notification.Position.TOP_START);
            LoginController.getInstance().loginAsUnregisteredRenter();
            continue_as_an_unregistered_renter.getUI().ifPresent(ui -> ui.navigate(MainView.class));
            // TODO take unreg renter to correct page
        });
        return continue_as_an_unregistered_renter;
    }

    private void addloginListener(LoginForm component) {
        component.addLoginListener(e -> {
            Notification.show("Logging in....", 3000, Notification.Position.TOP_START);
            boolean isAuthenticated = LoginController.getInstance().authenticateUser(e);
            if (isAuthenticated) {
                component.getUI().ifPresent(ui -> ui.getPage().reload()); // TODO take to correct page
            } else {
                component.setError(true);
                Notification.show("Incorrect username and password combination", 3000, Notification.Position.TOP_START);
            }
        });
    }

    private void addRegistrationListener(LoginForm component) {
        component.addForgotPasswordListener(e -> {
            // TODO route to registration page
            Notification.show("Taking you to the registration page....", 3000, Notification.Position.TOP_START);
            component.getUI().ifPresent(ui -> ui.navigate(RegistrationView.class));
        });
    }

    private void addCustomI81n(LoginForm component) {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setForgotPassword("Register");
        i18n.setAdditionalInformation("To close the login form, either submit non-empty username and password, register, or continue as an unregistered renter");
        i18n.getForm().getForgotPassword();
        component.setForgotPasswordButtonVisible(true);
        component.setI18n(i18n);
    }

    private void checkIfLoggedIn(LoginForm component) {
        LoginController loginController = LoginController.getInstance();
        if (loginController.isLoggedIn()) {
            Notification.show("Welcome!", 3000, Notification.Position.TOP_START);
            component.getUI().ifPresent(ui -> ui.navigate(MainView.class));
        }
    }

}

