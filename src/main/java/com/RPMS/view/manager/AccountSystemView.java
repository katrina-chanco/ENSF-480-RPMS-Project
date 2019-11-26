package com.RPMS.view.manager;

import com.RPMS.MainView;
import com.RPMS.controller.LoginController;
import com.RPMS.controller.PropertyController;
import com.RPMS.controller.manager.AccountController;
import com.RPMS.model.entity.Account;
import com.RPMS.model.entity.Property;
import com.RPMS.view.helpers.GridHelpers;
import com.RPMS.view.property.ViewPropertyDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Route(value = "manager/AccountSystem", layout = MainView.class)
public class AccountSystemView extends Div {

    private Dialog editAccountsDialog;
    private List<Account> accList;
    private AccountController accountController;
    private Grid<Account> gridResult = new Grid<>();
    private List<Account> dataCols;

    public AccountSystemView(){
        Label instruction = new Label("Displaying Access System Information Page");

        add(new VerticalLayout(
                new HorizontalLayout(instruction),
                gridResult
        ));

        accountController = AccountController.getInstance();
        accList = accountController.getAccount();
        gridResult.addItemClickListener(this::showGridEditDialog);
        gridResult.removeAllColumns();
        dataCols = new ArrayList<>(accList);

        gridResult.setItems(dataCols);
        gridResult.addColumn((Account::getId)).setAutoWidth(true).setHeader("Account ID");
        gridResult.addColumn(GridHelpers.getAccountLabel()).setAutoWidth(true).setHeader("Account Type");
        gridResult.addColumn((Account::getPassword)).setAutoWidth(true).setHeader("Password");
        gridResult.addColumn((Account::getName)).setAutoWidth(true).setHeader("Name");
        gridResult.addColumn((Account::getAddress)).setAutoWidth(true).setHeader("Address");
    }

    private void showGridEditDialog(ItemClickEvent itemClickEvent) {
//        Notification.show(itemClickEvent.getItem().toString());
        editAccountsDialog = new EditAccountsDialog((Account) itemClickEvent.getItem());
        editAccountsDialog.open();
        editAccountsDialog.addOpenedChangeListener( e -> updateGrid());
    }

    private void updateGrid() {
        try {
            accList = AccountController.getInstance().getAccount();
            gridResult.setItems(accList);
        } catch (NoResultException ex) {
            Notification.show("No results");
        }
    }
}
