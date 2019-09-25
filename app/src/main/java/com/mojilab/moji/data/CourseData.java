package com.mojilab.moji.data;

import java.util.ArrayList;

public class CourseData {
    public String mainAddress;
    public String subAddress;
    public String visitTime;
    public String content;
    public int order;
    public float lat;
    public float log;
    public ArrayList<String> photos;
    public ArrayList<Integer> share;

    public CourseData(){
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.visitTime = visitTime;
        this.content = content;
        this.order = order;
        this.lat = lat;
        this.log = log;
        this.photos = photos;
        this.share = share;
    }

    public CourseData(String mainAddress, String subAddress, String visitTime, String content, int order, float lat, float log, ArrayList<String> photos, ArrayList<Integer> share){
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.visitTime = visitTime;
        this.content = content;
        this.order = order;
        this.lat = lat;
        this.log = log;
        this.photos = photos;
        this.share = share;
    }
}
