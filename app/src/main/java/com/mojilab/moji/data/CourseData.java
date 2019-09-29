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
    public float lat;
    public float log;
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
        this.log = log;
        this.photos = photos;
        this.share = share;
    }

    public CourseData(int id, String mainAddress, String subAddress, String visitTime, String content, String tag, int order, float lat, float log, ArrayList<String> photos, ArrayList<Integer> share){
        this.id = id;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.visitTime = visitTime;
        this.content = content;
        this.tag = tag;
        this.order = order;
        this.lat = lat;
        this.log = log;
        this.photos = photos;
        this.share = share;
    }
}
