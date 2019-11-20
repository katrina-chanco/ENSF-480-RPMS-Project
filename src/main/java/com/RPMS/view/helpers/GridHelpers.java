package com.RPMS.view.helpers;

import com.RPMS.model.entity.Property;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.text.NumberFormat;
import java.util.Currency;

public class GridHelpers {
    public static ComponentRenderer<Span, Property> getPropertyPetBadge() {
        return new ComponentRenderer<>(person -> {
            Span badge = new Span();
            badge.setClassName("badge badge--small ");
            switch (person.getPetsAllowed()) {
                case CATS_ALLOWED:
                    badge.setText("Cats");
                    badge.setClassName(badge.getClassName() + " badge--success");
                    break;
                case DOGS_ALLOWED:
                    badge.setText("Dogs");
                    badge.setClassName(badge.getClassName() + " badge--success");
                    break;
                case DOGS_AND_CATS_ALLOWED:
                    badge.setText("Cats/Dogs");
                    badge.setClassName(badge.getClassName() + " badge--success");
                    break;
                case NO_PETS_ALLOWED:
                    badge.setText("Prohibited");
                    badge.setClassName(badge.getClassName() + " badge--danger");
                    break;
            }
            return badge;
        });
    }

    public static ComponentRenderer<Span, Property> getPropertyStatusBadge() {
        return new ComponentRenderer<>(property -> {
            Span badge = new Span();
            badge.setClassName("badge badge--small ");
            switch (property.getPropertyStatus()){
                case ACTIVE:
                    badge.setText("Active");
                    badge.setClassName(badge.getClassName() + " badge--success");
                    break;
                case RENTED:
                    badge.setText("Rented");
                    badge.setClassName(badge.getClassName() + " badge--info");
                    break;
                case CANCELED:
                    badge.setText("Canceled");
                    badge.setClassName(badge.getClassName() + " badge--danger");
                    break;
                case SUSPENDED:
                    badge.setText("Suspended");
                    badge.setClassName(badge.getClassName() + " badge--warning");
                    break;
            }
            return badge;
        });
    }

    public static ComponentRenderer<Label, Property> getPropertyCost() {
        return new ComponentRenderer<>(property -> {
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
            return new Label(defaultFormat.format(property.getPrice()));
        });
    }

    public static ComponentRenderer<HorizontalLayout, Property> getPropertyContract() {

        return new ComponentRenderer<>(property -> {
            Label label = new Label();
            Icon icon;
            if(!property.hasContract()){
                icon = new Icon(VaadinIcon.FILE_REMOVE);
                label.setText("No Contract");
            } else if(property.getContract().isSigned()) {
                icon = new Icon(VaadinIcon.FILE_TEXT);
                label.setText("Signed");
            } else {
                icon = new Icon(VaadinIcon.FILE_TEXT_O);
                label.setText("Pending");
            }
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.add(icon, label);
            return horizontalLayout;
        });
    }
}

