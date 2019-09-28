package com.mojilab.moji.data;

import java.util.ArrayList;

public class CourseUploadData {
    public String mainAddress;
    public String subAddress;
    public String visitTime;
    public String content;
    public ArrayList<String> tagInfo;
    public int order;
    public float lat;
    public float log;
    public ArrayList<PhotosData> photos;

    public CourseUploadData(CourseData courseData, ArrayList<PhotosData> photosDataArrayList, ArrayList<String> tagInfo){
        this.mainAddress = courseData.mainAddress;
        this.subAddress = courseData.subAddress;
        this.visitTime = courseData.visitTime;
        this.content = courseData.content;
        this.tagInfo = tagInfo;
        this.order =courseData.order;
        this.lat = courseData.lat;
        this.log = courseData.log;
        this.photos = photosDataArrayList;
    }
}
