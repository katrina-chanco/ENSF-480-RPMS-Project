package com.RPMS.model.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Entity
@NamedQueries({
        @NamedQuery(name="Property.findAllForLandlord", query="SELECT c FROM Property c WHERE c.landlord = :landlord"),
        @NamedQuery(name="Property.findAllByQuery", query="SELECT c FROM Property c " +
                "WHERE (c.price >= :lowerPrice OR :lowerPrice is null) " +
                "AND (c.price <= :upperPrice OR :upperPrice is null)" +
                "AND (c.bathrooms >= :lowerBath OR :lowerBath is null) " +
                "AND (c.bathrooms <= :upperBath OR :upperBath is null)" +
                "AND (c.beds >= :lowerBed OR :lowerBed is null) " +
                "AND (c.beds <= :upperBed OR :upperBed is null)" +
                "AND (c.address.city = :city OR :city is null OR :city = '')" +
                "AND (c.address.province = :province OR :province is null OR :province = '')" +
                "AND (c.address.postalCode = :postal OR :postal is null OR :postal = '')" +
                "AND (c.address.country = :country OR :country is null OR :country = '')" +
                "AND (c.petsAllowed = :pets_allowed OR :pets_allowed is null OR :pets_allowed = '')" +
                "AND c.propertyStatus = 'ACTIVE'")
})

@Table
public class Property {

    public enum Pets_Allowed{
        DOGS_ALLOWED("Dogs"),
        CATS_ALLOWED("Cats"),
        DOGS_AND_CATS_ALLOWED("Dogs/Cats"),
        NO_PETS_ALLOWED("Prohibited");

        private String prettyName;

        Pets_Allowed(String prettyName) {
            this.prettyName = prettyName;
        }

        public String getPrettyName() {
            return prettyName;
        }
    }
    public enum Property_Status{
        ACTIVE("Active"),
        CANCELED("Canceled"),
        RENTED("Rented"),
        SUSPENDED("Suspended");
        private String prettyName;

        Property_Status(String prettyName) {
            this.prettyName = prettyName;
        }

        public String getPrettyName() {
            return prettyName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double price;
    private int beds;
    private int bathrooms;
    private Date dateAdded;
    private Date dateActivated;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Amenity> amenities = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Image> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Pets_Allowed petsAllowed;

    @OneToOne(cascade = {CascadeType.ALL})
    private Landlord landlord;

    @OneToOne(cascade = {CascadeType.ALL})
    private Contract contract;

    @OneToOne(cascade = {CascadeType.ALL})
    private Address address;

    @Enumerated(EnumType.STRING)
    private Property_Status propertyStatus;

    public Property(Landlord landlord, Contract contract, Address address) {
        this.landlord = landlord;
        this.contract = contract;
        this.address = address;
    }

    public Property(double price, int beds, int bathrooms, List<Amenity> amenities, Pets_Allowed petsAllowed, Landlord landlord, Contract contract, Address address, Date dateAdded, Date dateActivated, Property_Status propertyStatus, List<Image> images) {
        this.price = price;
        this.beds = beds;
        this.bathrooms = bathrooms;
        this.amenities = amenities;
        this.petsAllowed = petsAllowed;
        this.landlord = landlord;
        this.contract = contract;
        this.address = address;
        this.dateAdded = dateAdded;
        this.dateActivated = dateActivated;
        this.propertyStatus = propertyStatus;
        this.images = images;
    }

    public Property(){

    }

    public Landlord getLandlord() {
        return landlord;
    }

    public void setLandlord(Landlord landlord) {
        this.landlord = landlord;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public Pets_Allowed getPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(Pets_Allowed petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Property_Status getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(Property_Status propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public boolean hasContract() {
        return contract != null;
    }

    public Date getDateActivated() {
        return dateActivated;
    }

    public void setDateActivated(Date dateActivated) {
        this.dateActivated = dateActivated;
    }

}
