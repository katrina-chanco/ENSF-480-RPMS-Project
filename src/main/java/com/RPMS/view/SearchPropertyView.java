package com.RPMS.view;

import com.RPMS.MainView;
import com.RPMS.model.entity.Property;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

import java.util.List;


@Route(value = "search", layout = MainView.class)
public class SearchPropertyView extends Div {
    public SearchPropertyView() {
        Button button = new Button("Vaadin button");

        button.addClickListener(e-> {
//            PropertCoyjnertkg propCOntroller = PropertCoyjnertkg.getInstance();
//            List<Property> propertyList = propCOntroller.listAllPropertires();
            Notification.show("Yeets");
        });

        Label label = new Label("SPV");
        add(label, button);
    }

}
