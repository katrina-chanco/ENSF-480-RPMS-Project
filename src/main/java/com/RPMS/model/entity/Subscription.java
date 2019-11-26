package com.RPMS.model.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table
@NamedQuery(name = "Subscription.getAll", query = "SELECT s FROM Subscription s")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = {CascadeType.ALL})
    private Registered_Renter subscriber;

    @ElementCollection
    private Map<String, String> propertyAttributes = new HashMap<>();

    public Subscription(Registered_Renter subscriber) {
        this.subscriber = subscriber;
    }

    public Subscription() {

    }

    public Subscription(Registered_Renter renter, Map<String, Object> propertyAttributes) {
        this.subscriber = renter;
        this.propertyAttributes = convertMapString(propertyAttributes);
    }

    public Map<String, String> getMap() {
        return propertyAttributes;
    }

    public Map<String, Object> convertMapObject(Map<String, String> stringStringMap) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        if (stringStringMap.get("lowerPrice") != null)
            stringObjectMap.put("lowerPrice", Double.parseDouble(stringStringMap.get("lowerPrice")));
        if (stringStringMap.get("upperPrice") != null)
            stringObjectMap.put("upperPrice", Double.parseDouble(stringStringMap.get("upperPrice")));
        if (stringStringMap.get("lowerBath") != null)
            stringObjectMap.put("lowerBath", Integer.parseInt(stringStringMap.get("lowerBath")));
        if (stringStringMap.get("upperBath") != null)
            stringObjectMap.put("upperBath", Integer.parseInt(stringStringMap.get("upperBath")));
        if (stringStringMap.get("lowerBed") != null)
            stringObjectMap.put("lowerBed", Integer.parseInt(stringStringMap.get("lowerBed")));
        if(stringStringMap.get("upperBed") != null)
            stringObjectMap.put("upperBed", Integer.parseInt(stringStringMap.get("upperBed")));
        if (stringStringMap.get("city") != null)
            stringObjectMap.put("city", stringStringMap.get("city"));
        if (stringStringMap.get("province") != null)
            stringObjectMap.put("province", stringStringMap.get("province"));
        if (stringStringMap.get("postal") != null)
            stringObjectMap.put("postal", stringStringMap.get("postal"));
        if (stringStringMap.get("country") != null)
            stringObjectMap.put("country", stringStringMap.get("country"));
        if (stringStringMap.get("pets_allowed") != null)
            stringObjectMap.put("pets_allowed", Property.Pets_Allowed.valueOf(stringStringMap.get("pets_allowed")));

        return stringObjectMap;
    }

    public Map<String, String> convertMapString(Map<String, Object> stringObjectMap) {
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("lowerPrice", null);
        stringStringMap.put("upperPrice", null);
        stringStringMap.put("lowerBath", null);
        stringStringMap.put("upperBath", null);
        stringStringMap.put("lowerBed", null);
        stringStringMap.put("upperBed", null);
        stringStringMap.put("city", null);
        stringStringMap.put("province", null);
        stringStringMap.put("postal", null);
        stringStringMap.put("country", null);
        stringStringMap.put("pets_allowed", null);

        if (stringObjectMap.get("lowerPrice") != null) {
            stringStringMap.put("lowerPrice", String.valueOf(stringObjectMap.get("lowerPrice")));
        }
        if (stringObjectMap.get("upperPrice") != null) {
            stringStringMap.put("upperPrice", String.valueOf(stringObjectMap.get("upperPrice")));
        }
        if (stringObjectMap.get("lowerBath") != null) {
            stringStringMap.put("lowerBath", String.valueOf(stringObjectMap.get("lowerBath")));
        }
        if (stringObjectMap.get("upperBath") != null) {
            stringStringMap.put("upperBath", String.valueOf(stringObjectMap.get("upperBath")));
        }
        if (stringObjectMap.get("lowerBed") != null) {
            stringStringMap.put("lowerBed", String.valueOf(stringObjectMap.get("lowerBed")));
        }
        if (stringObjectMap.get("upperBed") != null) {
            stringStringMap.put("upperBed", String.valueOf(stringObjectMap.get("upperBed")));
        }
        if (stringObjectMap.get("city") != null) {
            stringStringMap.put("city", (String) stringObjectMap.get("city"));
        }
        if (stringObjectMap.get("province") != null) {
            stringStringMap.put("province", (String) stringObjectMap.get("province"));
        }
        if (stringObjectMap.get("postal") != null) {
            stringStringMap.put("postal", (String) stringObjectMap.get("postal"));
        }
        if (stringObjectMap.get("country") != null) {
            stringStringMap.put("country", (String) stringObjectMap.get("country"));
        }
        if (stringObjectMap.get("pets_allowed") != null) {
            stringStringMap.put("pets_allowed", (String) stringObjectMap.get("pets_allowed"));
        }
        return stringStringMap;
    }

    public void setMap(Map<String, String> propertyAttributes) {
        this.propertyAttributes = propertyAttributes;
    }

    public Registered_Renter getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Registered_Renter subscriber) {
        this.subscriber = subscriber;
    }
}
