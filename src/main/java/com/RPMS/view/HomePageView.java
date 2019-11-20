package com.RPMS.view;

import com.RPMS.MainView;
import com.RPMS.controller.ContactStrategy.EmailStrategy;
import com.RPMS.controller.LoginController;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(value = "", layout = MainView.class)
public class HomePageView extends Div {
    public HomePageView() {
        Label label = new Label("HOME");
        add(label);
        checkLoggedIn();
    }

    private void checkLoggedIn() {
        if (!LoginController.getInstance().isLoggedIn() && !LoginController.getInstance().isLoggedInAsTemporaryUser()) {
            this.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        }
    }


}