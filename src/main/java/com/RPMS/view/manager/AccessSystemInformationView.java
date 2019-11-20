package com.RPMS.view.manager;

import com.RPMS.MainView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;

@Route(value = "manager/SelectSystemOptions/AccessSystemInformation", layout = MainView.class)
public class AccessSystemInformationView extends Div {
    public AccessSystemInformationView(){
        Label label = new Label("Displaying Access System Information Page");
        add(label);
    }
}
