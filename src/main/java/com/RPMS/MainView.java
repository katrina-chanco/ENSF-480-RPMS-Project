package com.RPMS;

import com.RPMS.view.HomePageView;
import com.RPMS.view.SearchPropertyView;
import com.RPMS.view.landlord.ListPropertyView;
import com.RPMS.view.manager.SelectSystemOptionsView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.RouterLink;


@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")

public class MainView extends AppLayout {

    public MainView(){
        final DrawerToggle drawerToggle = new DrawerToggle();
        final RouterLink home = new RouterLink("Home", HomePageView.class);
        final RouterLink about = new RouterLink("About Company", SearchPropertyView.class);
        final RouterLink landlordList = new RouterLink("Properties Listed", ListPropertyView.class);
        final VerticalLayout layout = new VerticalLayout(home, landlordList);
        final RouterLink selectSystemOptions = new RouterLink("Select System Options", SelectSystemOptionsView.class);
        final VerticalLayout layout = new VerticalLayout(home, about, selectSystemOptions);
        addToDrawer(layout);
        addToNavbar(drawerToggle);
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
