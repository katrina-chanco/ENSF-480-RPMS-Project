package com.RPMS.controller.landlord;

import com.RPMS.controller.PropertyController;
import com.RPMS.model.entity.Landlord;
import com.RPMS.model.entity.Property;

import java.util.List;

public class LandlordController {
    /**
     * Singleton pattern
     */
    private static LandlordController instance;

    private LandlordController() {
    }

    public static LandlordController getInstance() {
        if(instance == null) {
            instance = new LandlordController();
        }
        return instance;
    }

    public List<Property> getAllLandlordProperties(Landlord landlord) {
        PropertyController propertyController = PropertyController.getInstance();
        return propertyController.getAllLandlordProperties(landlord);
    }
}
