package com.RPMS.view.manager;

import com.RPMS.MainView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;

@Route(value = "manager/SelectSystemOptions/ChangeListingState", layout = MainView.class)
public class ChangeListingStateView extends Div {
    public ChangeListingStateView(){
        Label label = new Label("Displaying Change Listing State Page");
        add(label);
    }
}
