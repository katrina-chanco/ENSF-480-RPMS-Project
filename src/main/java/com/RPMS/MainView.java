package com.RPMS;

import com.RPMS.controller.LoginController;
import com.RPMS.view.HomePageView;
import com.RPMS.view.SearchPropertyView;
import com.RPMS.view.landlord.LandlordListPropertyView;
import com.RPMS.view.login_registration.LoginView;
import com.RPMS.view.manager.SelectSystemOptionsView;
import com.RPMS.view.renter.RenterSearchPropertyView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;

@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")

public class MainView extends AppLayout {

    /**
     * Instantiates MainView
     */
    public MainView() {
        DrawerToggle drawerToggle = new DrawerToggle();
        RouterLink selectSystemOptions = new RouterLink("Select System Options", SelectSystemOptionsView.class);
        RouterLink landlordList = new RouterLink("Properties Listed", LandlordListPropertyView.class);
        RouterLink renterSearch = new RouterLink("Find Your Home", RenterSearchPropertyView.class);
        RouterLink about = new RouterLink("About Company", SearchPropertyView.class);
        RouterLink home = new RouterLink("Home", HomePageView.class);
        VerticalLayout mainLayout = new VerticalLayout(home, about, selectSystemOptions, landlordList, renterSearch);
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


//
///**
// * The main view contains a button and a click listener.
// */
//@Route
//@PWA(name = "My Application", shortName = "My Application")
//public class MainView extends AppLayout {
//
//    private static EntityManagerFactory factory;
//    public MainView() {
//        Label tLabel = new Label("H(III");
//        AppLayout appLayout = new AppLayout();
//
//        appLayout.addToNavbar(new DrawerToggle());
//        Tabs tabs = new Tabs(new Tab("Login"), new Tab("Browse"));
//        tabs.setOrientation(Tabs.Orientation.VERTICAL);
//        appLayout.addToDrawer(tabs);
//        appLayout.setContent(tLabel);
//
//        appLayout.setVisible(true);
//
////        NaviBarView topNaviBar = new NaviBarView();
////
////        setSizeFull();
////        setMargin(false);
////        setSpacing(false);
////        setPadding(false);
////        add(header, workspace, footer);
////        Button button = new Button("Click me",
////                event ->{
////                    factory = Persistence.createEntityManagerFactory("RPMS_PU");
////                    EntityManager em = factory.createEntityManager();
////                    em.getTransaction().begin();
////
////                    Account account = new Manager(new Address(1,"Street", "City", "Postal", "Canada"),
////                            new Name("F", "M", "L"),
////                            new Email("email@email.ca"), "TESTMAN");
////
////                    em.persist(account);
////                    em.getTransaction().commit();
////                    em.close();
////                });
////        add(button);
//
//
//
//
//    }
//}
