package com.RPMS.view;

import com.RPMS.MainView;
import com.RPMS.controller.LoginController;
import com.RPMS.view.login_registration.LoginView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainView.class)
public class HomePageView extends Div implements BeforeEnterObserver {

    public HomePageView() {
            Label label = new Label("HOME");
            add(label);
        }

        /**
         * Callback executed before navigation to attaching Component chain is made.
         *
         * @param event before navigation event with event details
         */
        @Override
        public void beforeEnter (BeforeEnterEvent event){
            if (!LoginController.getInstance().isLoggedIn() && !LoginController.getInstance().isLoggedInUnregisteredRenter()) {
                event.rerouteTo(LoginView.class);
        }
    }
}