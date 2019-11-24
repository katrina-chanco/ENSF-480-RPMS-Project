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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GridHelpers {
    private static String BATH_SVG = "<svg width=\"24\" height=\"24\" xmlns=\"http://www.w3.org/2000/svg\"> <title>Bathtub</title> <g> <title>background</title> <rect x=\"-1\" y=\"-1\" width=\"26\" height=\"26\" id=\"canvas_background\" fill=\"none\"/> </g> <g> <title>Bath</title> <path stroke=\"null\" d=\"m22.111817,9.243258l0,-4.682444a1.560815,1.560815 0 0 0 -1.560815,-1.560815l-1.560815,0a1.560815,1.560815 0 0 0 -1.525696,1.233043a1.170611,1.170611 0 0 0 -0.815526,1.108178l0,0.780407a0.390204,0.390204 0 0 0 0.390204,0.390204l1.560815,0a0.390204,0.390204 0 0 0 0.390204,-0.390204l0,-0.780407a1.170611,1.170611 0 0 0 -0.721877,-1.080864a0.780407,0.780407 0 0 1 0.721877,-0.47995l1.560815,0a0.780407,0.780407 0 0 1 0.780407,0.780407l0,4.682444l-19.510181,0a1.560815,1.560815 0 0 0 0,3.121629l0,4.682444a1.560815,1.560815 0 0 0 1.560815,1.560815l0,0.390204a1.170611,1.170611 0 0 0 2.341222,0l0,-0.390204l12.486516,0l0,0.390204a1.170611,1.170611 0 0 0 2.341222,0l0,-0.390204a1.560815,1.560815 0 0 0 1.560815,-1.560815l0,-4.682444a1.560815,1.560815 0 0 0 0,-3.121629zm-17.16896,4.682444l-0.780407,0l0,0.780407a0.390204,0.390204 0 0 1 -0.780407,0l0,-1.170611a0.390204,0.390204 0 0 1 0.390204,-0.390204l1.170611,0a0.390204,0.390204 0 0 1 0,0.780407zm15.608145,2.731425a0.390204,0.390204 0 0 1 -0.390204,0.390204l-1.170611,0a0.390204,0.390204 0 0 1 0,-0.780407l0.780407,0l0,-0.780407a0.390204,0.390204 0 0 1 0.780407,0l0,1.170611z\" id=\"svg_1\"/> </g></svg>";

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
            switch (property.getPropertyStatus()) {
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
            if (!property.hasContract()) {
                icon = new Icon(VaadinIcon.FILE_REMOVE);
                label.setText("No Contract");
//            } else if (property.getContract().isSigned()) {
            }else if(true) {
//                TODO update this
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
            if(property.getImages().isEmpty()) {
                return new HorizontalLayout(new Label("No Images"));
            }
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
            pImage.setSrc("frontend/img/property_uploads/" + property.getImages().get(0).getFileName());
            pImage.setMaxWidth("380px");

            pImagesList.addValueChangeListener(imageEvent -> pImage.setSrc("frontend/img/property_uploads/" + imageEvent.getValue().getFileName()));
            imageLayout.add(pImagesList, pImage);
            return imageLayout;
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

