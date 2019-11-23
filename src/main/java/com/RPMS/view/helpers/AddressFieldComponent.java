package com.RPMS.view.helpers;

import com.RPMS.model.entity.Address;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

import static com.vaadin.flow.component.textfield.Autocapitalize.WORDS;


public class AddressFieldComponent extends CustomField<Address> {
    private NumberField houseNoField;
    private TextField streetNameField;
    private TextField cityNameField;
    private TextField provinceField;
    private TextField postalField;
    private ComboBox<String> countryField;

    public AddressFieldComponent() {

        setLabel("Address");
        houseNoField = new NumberField();
        houseNoField.setLabel("House No");
        houseNoField.setWidth("100px");
//        houseNoField.set

        streetNameField = new TextField();
        streetNameField.setLabel("Street name");
        streetNameField.setWidth("253px");
        streetNameField.setAutocapitalize(WORDS);
        streetNameField.setRequired(true);

        cityNameField = new TextField();
        cityNameField.setLabel("City");
        cityNameField.setAutocapitalize(WORDS);
        cityNameField.setWidth("147px");
        cityNameField.setRequired(true);

        provinceField = new TextField();
        provinceField.setLabel("Province/State");
        provinceField.setAutocapitalize(WORDS);
        provinceField.setWidth("200px");
        provinceField.setRequired(true);

        postalField = new TextField();
        new CustomStringBlockFormatter(new int[]{3, 3}, new String[]{" "}, CustomStringBlockFormatter.ForceCase.UPPER, null, false).extend(postalField);
        postalField.setLabel("Postal/Zip");
        postalField.setWidth("150px");
        postalField.setRequired(true);

        countryField = new ComboBox<>();
        countryField.setLabel("Country");
        countryField.setItems("Canada", " USA");
        countryField.setWidth("150px");
        countryField.setRequired(true);

        add(new HorizontalLayout(houseNoField, streetNameField, cityNameField), new HorizontalLayout(provinceField, postalField, countryField));
    }

    public void setEnabled(Boolean status) {
        houseNoField.setEnabled(status);
        streetNameField.setEnabled(status);
        cityNameField.setEnabled(status);
        provinceField.setEnabled(status);
        postalField.setEnabled(status);
        countryField.setEnabled(status);
    }

    @Override
    public Address generateModelValue() {
        Address address = new Address();
        address.setHouseNo(houseNoField.getValue().intValue());
        address.setStrName(streetNameField.getValue());
        address.setCity(cityNameField.getValue());
        address.setProvince(provinceField.getValue());
        address.setPostalCode(postalField.getValue());
        address.setCountry(countryField.getValue());
        return address;
    }

    @Override
    public void setPresentationValue(Address address) {
        houseNoField.setValue((double) address.getHouseNo());
        streetNameField.setValue(address.getStrName());
        cityNameField.setValue(address.getCity());
        provinceField.setValue(address.getProvince());
        postalField.setValue(address.getPostalCode());
        countryField.setValue(address.getCountry());
    }



//
//    /**
//     * Bind address fields
//     */
//    private void setAddressBinder() {
////        HNO
//        addressBinder.forField(houseNoField)
//                // define the validator
//                .withValidator(houseNo -> houseNo.length() != 0, "House number must contain at least one digit")
//                // define how the validation status is displayed
//                .withValidationStatusHandler(status -> {
//                    houseNoField.setErrorMessage(status.getMessage().orElse(""));
////                    houseNoField.er
////                    houseNoStatus.setText(status.getMessage().orElse(""));
////                    houseNoStatus.setVisible(status.isError());
//                })
//                .withConverter(new StringToIntegerConverter("Must enter a number"))
//                .bind(Address::getHouseNo, Address::setHouseNo);
//
////        STREET
//        addressBinder.forField(streetNameField)
//                // define the validator
//                .withValidator(streetName -> streetName.length() >= 2, "Street name must contain at least two characters")
//                // define how the validation status is displayed
////                .withValidationStatusHandler(status -> {
//////                    streetNameField.setErrorMessage("W");
////                    streetNameField.setErrorMessage(status.getMessage().orElse(""));
////                    streetNameField.setErrorMessage("hi");
//////                    strNameStatus.setVisible(status.isError());
////                })
//                .bind(Address::getStrName, Address::setStrName);
//
////
//    }


}
//
//
//    // ADDRESS
//// House number
////    Label houseNoStatus = new Label();
////    TextField houseNoField = new TextField();
//        addressBinder.forField(houseNoField)
//                // define the validator
//                .withValidator(houseNo -> houseNo.length() != 0, "House number must contain at least one digit")
//                // define how the validation status is displayed
//                .withValidationStatusHandler(status -> {
//                houseNoStatus.setText(status.getMessage().orElse(""));
//                houseNoStatus.setVisible(status.isError());
//                })
//                .withConverter(new StringToIntegerConverter("Must enter a number"))
//                .bind(Address::getHouseNo, Address::setHouseNo);
//                form.addFormItem(houseNoField, "House number");
//                form.addFormItem(houseNoStatus, "");
//// Street name
//                Label strNameStatus = new Label();
//                TextField streetNameField = new TextField();
//                addressBinder.forField(streetNameField)
//                // define the validator
//                .withValidator(streetName -> streetName.length() >= 2, "Street name must contain at least two characters")
//                // define how the validation status is displayed
//                .withValidationStatusHandler(status -> {
//                houseNoStatus.setText(status.getMessage().orElse(""));
//                houseNoStatus.setVisible(status.isError());
//                })
//                .bind(Address::getStrName, Address::setStrName);
//                form.addFormItem(streetNameField, "Street name");
//                form.addFormItem(strNameStatus, "");
//// City
//                Label cityStatus = new Label();
//                TextField cityField = new TextField();
//                addressBinder.forField(cityField)
//                // define the validator
//                .withValidator(city -> city.length() >= 3, "City name must contain at least three characters")
//                // define how the validation status is displayed
//                .withValidationStatusHandler(status -> {
//                cityStatus.setText(status.getMessage().orElse(""));
//                cityStatus.setVisible(status.isError());
//                })
//                .bind(Address::getCity, Address::setCity);
//                form.addFormItem(cityField, "City name");
//                form.addFormItem(cityStatus, "");
// Postal code
//                Label postalStatus = new Label();
//                TextField postalField = new TextField();
//                new CustomStringBlockFormatter(new int[]{3, 3}, new String[]{" "}, CustomStringBlockFormatter.ForceCase.UPPER, null, false).extend(postalField);
//                addressBinder.forField(houseNoField)
//                // define the validator
//                .withValidator(houseNo -> houseNo.length() >= 6, "Postal code must contain six characters")
//                // define how the validation status is displayed
//                .withValidationStatusHandler(status -> {
//                postalStatus.setText(status.getMessage().orElse(""));
//                postalStatus.setVisible(status.isError());
//                })
//                .bind(Address::getPostalCode, Address::setPostalCode);
//                form.addFormItem(postalField, "Postal code");
//                form.addFormItem(postalStatus, "");
//// Country
//                Label countryStatus = new Label();
//                TextField countryField = new TextField();
//                addressBinder.forField(cityField)
//                // define the validator
//                .withValidator(city -> city.length() >= 3, "Country name must contain at least three characters")
//                // define how the validation status is displayed
//                .withValidationStatusHandler(status -> {
//                countryStatus.setText(status.getMessage().orElse(""));
//                countryStatus.setVisible(status.isError());
//                })
//                .bind(Address::getCity, Address::setCity);
//                form.addFormItem(countryField, "City name");
//                form.addFormItem(countryStatus, "");
