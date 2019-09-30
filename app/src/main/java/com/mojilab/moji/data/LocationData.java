package com.mojilab.moji.data;

import java.util.ArrayList;

public class LocationData {
    public String mainAddress;
    public String subAddress;
    public double lat;
    public double lng;

    public LocationData(){
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.lat = lat;
        this.lng = lng;
    }

    public LocationData(String mainAddress, String subAddress, double lat, double lng){
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.lat = lat;
        this.lng = lng;
    }
}
