package com.mojilab.moji.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Courses {

    @SerializedName("mainAddress")
    @Expose
    private String mainAddress;
    @SerializedName("subAddress")
    @Expose
    private String subAddress;
    @SerializedName("visitTime")
    @Expose
    private String visitTime;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("tagInfo")
    @Expose
    private ArrayList<String> tagInfo;
    @SerializedName("order")
    @Expose
    private int order;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;


    public Courses(String mainAddress, String subAddress, String visitTime, String content, ArrayList<String> tagInfo, int order, double lat, double lng) {
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.visitTime = visitTime;
        this.content = content;
        this.tagInfo = tagInfo;
        this.order = order;
        this.lat = lat;
        this.lng = lng;
    }

    // other constructors, getter and setter methods
}