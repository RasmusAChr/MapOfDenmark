package com.telos.mapofdenmark;

import java.io.Serializable;

public class Member implements Serializable{
    String Type;
    long ref;
    Way way;

    public Member(String Type,long ref){
        this.Type=Type;
        this.ref=ref;

    }
    public String getType(){
        return Type;
    }
    public long getRef(){
        return ref;
    }
    public void setWay(Way way){
        this.way=way;
    }
    public Way getWay(){
        return way;
    }

}