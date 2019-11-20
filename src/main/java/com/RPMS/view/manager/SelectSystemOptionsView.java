package com.RPMS.view.manager;
import com.RPMS.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

@Route(value = "manager/SelectSystemOptions", layout = MainView.class)
public class SelectSystemOptionsView extends Div{
    public SelectSystemOptionsView(){
        Label label = new Label("Displaying Select System Options Page");
        add(label);

        Button bChangeListingState = new Button("Change Listing State");
        Button bModifyPayment = new Button("Modify Payment");
        Button bAccessSystemInformation = new Button("Access System Information");
        Button bRequestReport = new Button("Request Report");

        add(bChangeListingState, bModifyPayment, bAccessSystemInformation, bRequestReport);

        bChangeListingState.addClickListener(e-> {
            bChangeListingState.getUI().ifPresent(ui ->
                    ui.navigate(ChangeListingStateView.class));
            //Notification.show("Process of Changing the State");
        });

        bModifyPayment.addClickListener(e-> {
            bModifyPayment.getUI().ifPresent(ui ->
                    ui.navigate(ModifyPaymentView.class));
        });

        bAccessSystemInformation.addClickListener(e-> {
            bAccessSystemInformation.getUI().ifPresent(ui ->
                    ui.navigate(AccessSystemInformationView.class));
        });

        bRequestReport.addClickListener(e-> {
            bRequestReport.getUI().ifPresent(ui ->
                    ui.navigate(RequestReportView.class));
        });
    }
}
