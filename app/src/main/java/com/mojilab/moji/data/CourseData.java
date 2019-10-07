package com.mojilab.moji.data;

import java.util.ArrayList;

public class CourseData {
    public int id;
    public String mainAddress;
    public String subAddress;
    public String visitTime;
    public String content;
    public String tag;
    public int order;
    public Double lat;
    public Double lng;
    public ArrayList<String> photos;
    public ArrayList<Integer> share;

    public CourseData(){
        this.id = id;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.visitTime = visitTime;
        this.content = content;
        this.tag = tag;
        this.order = order;
        this.lat = lat;
        this.lng = lng;
        this.photos = photos;
        this.share = share;
    }

    public CourseData(int id, String mainAddress, String subAddress, String visitTime, String content, String tag, int order, Double lat, Double lng, ArrayList<String> photos, ArrayList<Integer> share){
        this.id = id;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.visitTime = visitTime;
        this.content = content;
        this.tag = tag;
        this.order = order;
        this.lat = lat;
        this.lng = lng;
        this.photos = photos;
        this.share = share;
    }


}
