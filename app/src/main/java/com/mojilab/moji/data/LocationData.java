package com.mojilab.moji.data;

import java.util.ArrayList;

public class LocationData {
    public String mainAddress;
    public String subAddress;
    public float lat;
    public float log;

    public LocationData(){
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.lat = lat;
        this.log = log;
    }

    public LocationData(String mainAddress, String subAddress, float lat, float log){
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.lat = lat;
        this.log = log;
    }
}
