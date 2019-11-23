package com.RPMS.view.login_registration;
import com.RPMS.controller.LoginController;
import com.RPMS.view.HomePageView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
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

    private H1 head;
    private VerticalLayout vl;
    private LoginForm form;
    private Button continue_as_an_unregistered_renter;

    public LoginView() {
        head = new H1("Welcome to RPMS");
        vl = new VerticalLayout();
        vl.add(head, loginForm(), unregisteredRenterButton());
        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl.setAlignItems(FlexComponent.Alignment.CENTER);
        vl.setAlignSelf(FlexComponent.Alignment.CENTER);
        vl.setWidth("100%");
        add(vl);
    }

    private Component loginForm() {
        form = new LoginForm();
        addCustomI81n();
        addLoginListener();
        addRegistrationListener();
        return form;
    }

    private Button unregisteredRenterButton() {
        continue_as_an_unregistered_renter = new Button("Continue as an unregistered renter");
        continue_as_an_unregistered_renter.setThemeName(Lumo.DARK);
        continue_as_an_unregistered_renter.setVisible(true);
        addRegisterClickListener();
        return continue_as_an_unregistered_renter;
    }


    private void addRegisterClickListener() {
        continue_as_an_unregistered_renter.addClickListener(e -> {
            Notification.show("Entering RPMS as an unregistered renter...", 3000, Notification.Position.TOP_START);
            LoginController.getInstance().loginAsUnregisteredRenter();
            continue_as_an_unregistered_renter.getUI().ifPresent(ui -> ui.navigate(HomePageView.class));
            // TODO take unreg renter to correct page
        });
    }

    private void addLoginListener() {
        form.addLoginListener(e -> {
            boolean isAuthenticated = LoginController.getInstance().authenticateUser(e);
            if (isAuthenticated) {
                Notification.show("Welcome!", 3000, Notification.Position.TOP_START);
                form.getUI().ifPresent(ui -> ui.navigate(HomePageView.class)); // TODO take to correct page
            } else {
                form.setError(true);
                Notification.show("Incorrect username and password combination", 3000, Notification.Position.TOP_START);
            }
        });
    }

    private void addRegistrationListener() {
        form.addForgotPasswordListener(e -> {
            // TODO route to registration page
            Notification.show("Taking you to the registration page....", 3000, Notification.Position.TOP_START);
            Dialog regDialog = new RegistrationView();
            regDialog.open();
        });
    }

    private void addCustomI81n() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setForgotPassword("Register");
        i18n.setAdditionalInformation("To close the login form, either submit non-empty username and password, register, or continue as an unregistered renter");
        i18n.getForm().getForgotPassword();
        form.setForgotPasswordButtonVisible(true);
        form.setI18n(i18n);
    }

}

