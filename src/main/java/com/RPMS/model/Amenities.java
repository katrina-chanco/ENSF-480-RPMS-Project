package com.RPMS.model;

/**
 * List of amenities
 */
public enum Amenities {
    WASHER_DRYER("Washer and Dryer"),
    AC("A/C"),
    FURNISHED("Fully Furnished"),
    PATIO("Patio"),
    HW_FLOOR("Hardwood Floors"),
    DISHWASHER("Dishwasher"),
    FIREPLACE("Fireplace"),
    WALK_IN_CLOSETS("Walk-In Closets"),
    WIFI("Wi-Fi"),
    GATED_COMM("Gated Community"),
    EX_BRICK("Brick Exterior"),
    EX_STONE("Stone Exterior"),
    EX_SIDING("Vinyl Exterior"),
    EX_STUCCO("Stucco Exterior"),
    ROOF_SHINGLE("Shingle Roof"),
    ROOF_Tile("Tile Roof"),
    ROOF_TAR("Tar Roof"),
    YARD_BACK("Backyard"),
    BBQ("BBQ"),
    UTILS("Utilities Included"),
    LAWN("Lawn"),
    KITCHEN_APP("Kitchen Appliances"),
    COUNTER_GRANITE("Granite Counter-tops"),
    COUNTER_MARBLE("Marble Counter-tops"),
    BATH_SINKS("Dual Bath Sinks"),
    JET_TUB("Jet Tub"),
    HEATED_FLOORS("Heated Floors"),
    SOLAR("Solar Installed"),
    ACC_DOOR("Accessible Doors"),
    ACC_RAMP("Accessible Ramp"),
    ACC_TUB("Accessible Tub"),
    UTILS_GAS("Gas Util Included"),
    UTILS_ELEC("Electric Utils Included");


    private final String amenity;
    Amenities(String amenity) {
        this.amenity = amenity;
    }

    public String getAmenityName() {
        return this.amenity;
    }

    public static Amenities fromString(String textVal) {
        for (Amenities b : Amenities.values()) {
            if (b.amenity.equalsIgnoreCase(textVal)) {
                return b;
            }
        }
        return null;
    }
}
