package com.RPMS.view;

import com.RPMS.MainView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainView.class)
public class HomePageView extends Div {
    public HomePageView() {
        Label label = new Label("HOME");
        add(label);

    }
}