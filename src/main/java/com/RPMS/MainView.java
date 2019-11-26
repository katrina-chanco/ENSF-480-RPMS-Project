package com.RPMS;

import com.RPMS.controller.LoginController;
import com.RPMS.view.HomePageView;
import com.RPMS.view.landlord.LandlordListPropertyView;
import com.RPMS.view.login_registration.LoginView;
import com.RPMS.view.manager.AccountSystemView;
import com.RPMS.view.manager.ModifyPaymentView;
import com.RPMS.view.manager.RequestReportView;
import com.RPMS.view.manager.SelectSystemOptionsView;
import com.RPMS.view.renter.RenterSearchPropertyView;
import com.RPMS.view.property.ListPropertyView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.ArrayList;

@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@StyleSheet("./styles/custom.css")
public class MainView extends AppLayout {
    public MainView(){

        DrawerToggle drawerToggle = new DrawerToggle();
        VerticalLayout mainLayout = new VerticalLayout();

        if (!LoginController.getInstance().isLoggedInUnregisteredRenter() && !LoginController.getInstance().isLoggedIn()) {
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        } else {
            if (LoginController.getInstance().isLoggedInUnregisteredRenter() || LoginController.getInstance().isAccountRenter()) {
                RouterLink renterSearch = new RouterLink("Find Your Home", RenterSearchPropertyView.class);
                mainLayout.add(renterSearch);
            } else {
                if (LoginController.getInstance().isAccountLandlord() || LoginController.getInstance().isAccountManager()) {
                    RouterLink listProperties = new RouterLink("Properties Listed", ListPropertyView.class);
                    mainLayout.add(listProperties);
                }
                if (LoginController.getInstance().isAccountManager()) {
                    RouterLink modifyPayment = new RouterLink("Modify Payment Settings", ModifyPaymentView.class);
                    RouterLink reports = new RouterLink("System Reports", RequestReportView.class);
                    RouterLink accountOptions = new RouterLink("Modify Accounts", AccountSystemView.class);
                    mainLayout.add(modifyPayment);
                    mainLayout.add(reports);
                    mainLayout.add(accountOptions);
                }
            }

        }


        addToDrawer(mainLayout);
        addToNavbar(drawerToggle);
        HorizontalLayout filler = new HorizontalLayout();
        filler.setWidth("88%");
        addToNavbar(filler);
        addToNavbar(logoutButton());
    }

    /**
     * Button to logout current user
     *
     * @return
     */
    private Button logoutButton() {
        Button logoutButton = new Button("Logout");
        logoutButton.addClickListener(e -> {
            LoginController.getInstance().logOutUser();
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });
        logoutButton.setThemeName(Lumo.DARK);
        logoutButton.setIcon(new Icon(VaadinIcon.EXIT_O));
        return logoutButton;
    }


}
