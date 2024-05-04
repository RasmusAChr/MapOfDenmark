package com.telos.mapofdenmark.TrieClasses;

import java.io.Serializable;
/**
 * The Address class represents an real-life address with details splitted up into different components, such as: street, house number, city, municipality and country.
 * The class provides functionality to create address objects but also manipulate them.
 * The class allows for putting and retrieving the individual components of an address, but also the option for the whole address.
 */
public class Address implements Serializable {
    final static long serialVersionUID = 241241251241L;
    String street;
    String houseNumber;
    String city;
    String municipality;
    String country;

    //Lupin 32 27xx Smørum Egedal Danmark
    /**
     * Constructor for the class. It creates address objects with empty individual components, and uses setter methods
     * to set street, houseNumber etc.
     */
    public Address() {
        this.street = "";
        this.houseNumber = "";
        this.city = "";
        this.municipality = "";
        this.country = "";
    }
    /**
     * Constructor for the class. It creates address objects with the individual components filled by parameters
     */
    public Address(String street, String houseNumber, String city, String municipality, String country){
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.municipality = municipality;
        this.country = country;
    }
    /**
     * Setter method to set the street name of the address
     * @param street - street name of the address
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Setter method to set the houseNumber of the address
     * @param houseNumber - houseNumber of the address
     */
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    /**
     * Setter method to set the city of the address
     * @param city - city of the address
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Setter method to set the municipality of the address
     * @param municipality - municipality of the address
     */
    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    /**
     * Setter method to set the country of the address
     * @param country - country of the address
     */
    public void setCountry(String country) { this.country = country; }

    /**
     * Getter method to retrieve the full address
     * @return - a string which represents the whole address
     */
    public String getFullAddress(){
        return street+" "+houseNumber+" "+city+" "+municipality+" "+country;
    }

//    public String getFullAddress() {
//        return capitalizeWords(street) + " " + houseNumber + ", " +
//                capitalizeWords(city) + ", " +
//                capitalizeWords(municipality) + ", " +
//                capitalizeWords(country);
//    }
//
//    // Helper method to capitalize each word in a string
//    private String capitalizeWords(String input) {
//        return input.substring(0, 1).toUpperCase() + input.substring(1);
//    }

    /**
     * Getter method to retrieve the street name of the address
     * @return - a string that represents the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Getter method to retrieve the city of the address
     * @return - a string that represents the city
     */
    public String getCity() {
        return city;
    }
}
