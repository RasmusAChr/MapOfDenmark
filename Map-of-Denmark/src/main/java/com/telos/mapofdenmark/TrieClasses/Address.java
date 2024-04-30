package com.telos.mapofdenmark.TrieClasses;

import java.io.Serializable;

public class Address implements Serializable {
    final static long serialVersionUID = 241241251241L;
    String street;
    String houseNumber;
    String city;
    String municipality;
    String country;
    //Lupin 32 27xx Smørum Egedal Danmark
    public Address() {
        this.street = "";
        this.houseNumber = "";
        this.city = "";
        this.municipality = "";
        this.country = "";
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getFullAdress(){
        return street+" "+houseNumber+" "+city+" "+municipality+" "+country;
    }
        public Address(String street, String houseNumber, String city, String municipality, String country){
        this.street = street;
        this.houseNumber = street;
        this.city = city;
        this.municipality = municipality;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }
}
