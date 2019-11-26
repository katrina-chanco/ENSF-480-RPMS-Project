package com.RPMS.view.manager;

import com.RPMS.MainView;
import com.RPMS.controller.manager.AccountController;
import com.RPMS.model.entity.Account;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "manager/AccountSystem", layout = MainView.class)
public class AccountSystemView extends Div {

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

        gridResult.removeAllColumns();
        dataCols = new ArrayList<>(accList);

        gridResult.setItems(dataCols);
        gridResult.addColumn((Account::getId)).setHeader("Account ID").setWidth("300px");
        //gridResult.addColumn((Account::getAccountType)).setHeader("Account Type").setWidth("300px");
        gridResult.addColumn((Account::getPassword)).setHeader("Password").setWidth("300px");
        gridResult.addColumn((Account::getName)).setHeader("Name").setWidth("300px");
        gridResult.addColumn((Account::getAddress)).setHeader("Address").setWidth("300px");
    }
}
