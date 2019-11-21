package com.RPMS.view.manager;

import com.RPMS.MainView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

@Route(value = "manager/SelectSystemOptions/ModifyPayment", layout = MainView.class)
public class ModifyPaymentView extends Div {
    public ModifyPaymentView(){
        Label label = new Label("Displaying Modify Payment Page");
        add(label);
    }
}
