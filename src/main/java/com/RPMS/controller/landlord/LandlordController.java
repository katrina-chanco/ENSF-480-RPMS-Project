package com.RPMS.controller.landlord;

import com.RPMS.controller.LoginController;
import com.RPMS.controller.PropertyController;
import com.RPMS.controller.SubscriptionController;
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

    public List<Property> getAllLandlordProperties() {
        PropertyController propertyController = PropertyController.getInstance();
        Landlord landlord  = (Landlord) LoginController.getInstance().getAccount();
        return propertyController.getAllLandlordProperties(landlord);
    }

    public void saveProperty(Property property) {
        property.setLandlord((Landlord) LoginController.getInstance().getAccount());
        PropertyController.getInstance().saveProperty(property);
        SubscriptionController.getInstance().notifySubscribers(property);
    }
}
