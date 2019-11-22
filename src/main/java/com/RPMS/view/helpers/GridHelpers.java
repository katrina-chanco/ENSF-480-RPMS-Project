package com.RPMS.view.helpers;

import com.RPMS.model.entity.Image;
import com.RPMS.model.entity.Property;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GridHelpers {
    private static String BATH_SVG = "<svg viewBox=\"0 0 16 16\" preserveAspectRatio=\"xMidYMid meet\" focusable=\"false\" style=\"pointer-events: none; display: block; width: 100%; height: 100%;\"><g><path d=\"m58,26l0,-12a4,4 0 0 0 -4,-4l-4,0a4,4 0 0 0 -3.91,3.16a3,3 0 0 0 -2.09,2.84l0,2a1,1 0 0 0 1,1l4,0a1,1 0 0 0 1,-1l0,-2a3,3 0 0 0 -1.85,-2.77a2,2 0 0 1 1.85,-1.23l4,0a2,2 0 0 1 2,2l0,12l-50,0a4,4 0 0 0 0,8l0,12a4,4 0 0 0 4,4l0,1a3,3 0 0 0 6,0l0,-1l32,0l0,1a3,3 0 0 0 6,0l0,-1a4,4 0 0 0 4,-4l0,-12a4,4 0 0 0 0,-8zm-44,12l-2,0l0,2a1,1 0 0 1 -2,0l0,-3a1,1 0 0 1 1,-1l3,0a1,1 0 0 1 0,2zm40,7a1,1 0 0 1 -1,1l-3,0a1,1 0 0 1 0,-2l2,0l0,-2a1,1 0 0 1 2,0l0,3z\"></path></g></svg>";
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

    public static ComponentRenderer<HorizontalLayout, Property> getImageList() {

        return new ComponentRenderer<>(property -> {
            HorizontalLayout imageLayout = new HorizontalLayout();
            ListBox<Image> pImagesList = new ListBox<>();
            List<Image> imageList = property.getImages();
            pImagesList.setItems(imageList);
            pImagesList.setValue(imageList.get(0));
            AtomicInteger imageCount = new AtomicInteger(1);
            pImagesList.setRenderer(new ComponentRenderer<>(item -> {
                Label label = new Label("Image " + imageCount);
                imageCount.getAndIncrement();
                return label;
            }));
            com.vaadin.flow.component.html.Image pImage = new com.vaadin.flow.component.html.Image();
            pImage.setAlt("Property Image");
            pImage.setSrc("frontend/img/property_uploads/"+property.getImages().get(0).getFileName());
            pImage.setMaxWidth("380px");

            pImagesList.addValueChangeListener(imageEvent -> pImage.setSrc("frontend/img/property_uploads/"+imageEvent.getValue().getFileName()));
            imageLayout.add(pImagesList, pImage);
            return  imageLayout;
        });
    }

    public static ComponentRenderer<HorizontalLayout, Property> getBeds() {

        return new ComponentRenderer<>(property -> {

            Icon bedIcon = new Icon(VaadinIcon.BED);
            Label pBeds = new Label(String.valueOf(property.getBeds()));
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.add(bedIcon, pBeds);
            return horizontalLayout;
        });
    }

    public static ComponentRenderer<HorizontalLayout, Property> getBathrooms() {

        return new ComponentRenderer<>(property -> {

            Label bathIcon = new Label();
            bathIcon.getElement().setProperty("innerHTML", BATH_SVG);
            Label pBath = new Label(String.valueOf(property.getBathrooms()));
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.add(bathIcon, pBath);
            return horizontalLayout;
        });
    }


}

