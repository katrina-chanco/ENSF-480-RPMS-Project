package com.RPMS.view.landlord;

import com.RPMS.controller.FileController;
import com.RPMS.controller.PropertyController;
import com.RPMS.controller.landlord.LandlordController;
import com.RPMS.model.Amenities;
import com.RPMS.model.entity.*;
import com.RPMS.view.helpers.AddressFieldComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.theme.lumo.Lumo;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.*;
import java.util.stream.Collectors;


public class LandlordAddEditPropertyDialog extends Dialog {
    private FormLayout formLayout;
    private Property property;
    private Button saveButton;
    private Button closeButton;
    private HorizontalLayout bottomBar;
    private Binder<Property> binder = new Binder<>(Property.class);

    private AddressFieldComponent addressField;
    private NumberField propertyPriceField;
    private NumberField propertyBedField;
    private NumberField propertyBathField;
    private ComboBox<Property.Pets_Allowed> propertyPetStatus;
    private MultiselectComboBox<Amenities> propertyAmenitiesField;
    private Upload upload;
    private ArrayList<Image> uploadedImages;

    private Converter<Double, Integer> doubleIntegerConverter = new Converter<Double, Integer>() {
        @Override
        public Result<Integer> convertToModel(Double aDouble, ValueContext valueContext) {
            return Result.ok(aDouble.intValue());
        }

        @Override
        public Double convertToPresentation(Integer integer, ValueContext valueContext) {
            return new Double(integer);
        }
    };

    private Converter<Set<Amenities>, List<Amenity>> amenityEnumConverter = new Converter<Set<Amenities>, List<Amenity>> () {

        @Override
        public Result<List<Amenity>> convertToModel(Set<Amenities> amenities, ValueContext valueContext) {
            List<Amenity> resultList =  amenities.stream().map(a-> new Amenity(a.getAmenityName())).collect(Collectors.toList());
            ArrayList<Amenity> amenityArrayList = (ArrayList<Amenity>) resultList;
            return Result.ok(amenityArrayList);
        }

        @Override
        public Set<Amenities> convertToPresentation(List<Amenity> amenities, ValueContext valueContext) {
            Set<Amenities> set = amenities.stream().map(a-> Amenities.fromString(a.getAttribute())).collect(Collectors.toSet());
            return amenities.stream().map(a-> Amenities.fromString(a.getAttribute())).collect(Collectors.toSet());
        }
    };

    public LandlordAddEditPropertyDialog(Property property) {
        this.property = property;
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setHeight("750px");
        setWidth("605px");
        VerticalLayout layout = new VerticalLayout();
        uploadedImages = new ArrayList<>();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        layout.setDefaultHorizontalComponentAlignment(
                FlexComponent.Alignment.STRETCH);

        //        Bottom buttons
        bottomBar = new HorizontalLayout();
        bottomBar.setHeight("50px");
        bottomBar.setWidth("100%");
        closeButton = new Button("Close");
        closeButton.addClickListener(e -> close());
        saveButton = new Button("Save");
        saveButton.setThemeName(Lumo.DARK);
        saveButton.addClickListener(e -> saveButton());

        Button testB = new Button("Test");
        testB.addClickListener(e -> {
            System.out.println(property.getAddress().toString());
            Notification.show("Stuff");
        });
        bottomBar.add(testB);

        bottomBar.add(closeButton, saveButton);

        createForm();

        formLayout.setMinHeight("675px");
        formLayout.setWidth("100%");

        layout.add(formLayout, bottomBar);
        add(layout);
    }

    public LandlordAddEditPropertyDialog() {
        this(PropertyController.getInstance().generatePropertyBean());
    }



    /**
     * Form layout
     */
    private void createForm() {
        formLayout = new FormLayout();

//        FIELDS
        addressField = new AddressFieldComponent();
        addressField.setRequiredIndicatorVisible(true);

        propertyPriceField = new NumberField("Cost/Month");
        propertyPriceField.setWidth("150px");

        propertyBedField = new NumberField("Beds");
        propertyBedField.setWidth("100px");
        propertyBedField.setMax(10);
        propertyBedField.setMin(1);
        propertyBedField.setHasControls(true);

        propertyBathField = new NumberField("Baths");
        propertyBathField.setWidth("100px");
        propertyBathField.setMax(10);
        propertyBathField.setMin(1);
        propertyBathField.setHasControls(true);

        propertyPetStatus = new ComboBox<>("Pets Allowed");
        propertyPetStatus.setWidth("133px");

        List<Property.Pets_Allowed> pets_allowedList = new ArrayList<>(EnumSet.allOf(Property.Pets_Allowed.class));

        propertyPetStatus.setDataProvider(new ListDataProvider<>(pets_allowedList));
        propertyPetStatus.setItemLabelGenerator(Property.Pets_Allowed::getPrettyName);

        propertyAmenitiesField = new MultiselectComboBox("Amenities");

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        upload = new Upload(buffer);
        upload.setDropLabel(new Label("Upload Property Images"));
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.addSucceededListener(event -> uploadedImages.add(new Image(FileController.UploadFiles(event, buffer))));
        upload.setWidth("495px");

        List<Amenities> amenitiesList = new ArrayList<>(EnumSet.allOf(Amenities.class));
        propertyAmenitiesField.setDataProvider(new ListDataProvider<>(amenitiesList));
        propertyAmenitiesField.setItemLabelGenerator(Amenities::getAmenityName);
        propertyAmenitiesField.setWidth("530px");


//        BINDERS
        binder.bind(addressField, Property::getAddress,
                Property::setAddress);

        binder.bind(propertyPriceField, Property::getPrice,
                Property::setPrice);

        binder.forField(propertyBedField).withConverter(doubleIntegerConverter).bind(Property::getBeds, Property::setBeds);

        binder.forField(propertyBathField).withConverter(doubleIntegerConverter).bind(Property::getBathrooms, Property::setBathrooms);

        binder.bind(propertyPetStatus, Property::getPetsAllowed,
                Property::setPetsAllowed);

        binder.forField(propertyAmenitiesField).withConverter(amenityEnumConverter).bind(Property::getAmenities, Property::setAmenities);


        // Updates the value in each bound field component
        binder.readBean(property);


        formLayout.add(new VerticalLayout(
                        addressField,
                        new HorizontalLayout(propertyPriceField, propertyBedField, propertyBathField, propertyPetStatus),
                        propertyAmenitiesField,
                        upload
                )
        );

    }


    private void formValid() throws Exception {
        if(addressField.isInvalid() || propertyBathField.isInvalid() || propertyBedField.isInvalid() || propertyPriceField.isInvalid() || propertyPetStatus.isInvalid() || propertyAmenitiesField.isInvalid()){
            throw new Exception();
        }
     }
    /**
     * update bean
     */
    private void saveButton() {
        property.setImages(uploadedImages);
        BinderValidationStatus binderValidationStatus = binder.validate();
        List validationResults =  binderValidationStatus.getValidationErrors();
        try {
            saveButton.setEnabled(false);
            binder.validate();
            binder.writeBean(property);
            LandlordController.getInstance().saveProperty(property);
//            formValid();
            // A real application would also save
            // the updated person
            // using the application's backend
//            System.out.println("Saved");
            close();
        } catch (ValidationException e) {
            saveButton.setEnabled(true);
            Notification.show("Please check all the fields.");
        } catch (Exception e) {
            saveButton.setEnabled(true);
            e.printStackTrace();
        }
    }


}
