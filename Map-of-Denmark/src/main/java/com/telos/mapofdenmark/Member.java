package com.telos.mapofdenmark;

import java.io.Serializable;

/**
 * Represents a member of a relation
 * This class contains information about the type of the member,
 * the reference ID, and optionally, a reference to a Way object.
 */
public class Member implements Serializable{
    // Type of the member
    String Type;
    // Reference to a Way object
    Way way;

    /**
     * Constructs a Member object with the specified type and reference ID.
     * @param Type The type of the member
     */
    public Member(String Type){
        this.Type=Type;
    }

    /**
     * Retrieves the type of the member.
     * @return The type of the member
     */
    public String getType(){
        return Type;
    }

    /**
     * Sets the reference to a Way object for the member.
     * @param way The Way object to set as reference
     */
    public void setWay(Way way){
        this.way=way;
    }

    /**
     * Retrieves the reference to the associated Way object.
     * @return The reference to the associated Way object
     */
    public Way getWay(){
        return way;
    }

}