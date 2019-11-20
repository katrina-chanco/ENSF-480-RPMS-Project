package com.RPMS.controller.ContactStrategy;

import com.RPMS.model.entity.Property;

public interface ContactStrategy {
    void contactLandlord(String message, Property property);
}
