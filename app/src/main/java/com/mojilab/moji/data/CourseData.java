package com.mojilab.moji.data;

import java.util.ArrayList;

public class CourseData {
    public int id;
    public int order;
    public String location;
    public String date;
    public ArrayList<String> img;
    public String contents;

    public CourseData(int id, int order, String location, String date, ArrayList<String> img, String contents){
        this.id = id;
        this.order = order;
        this.location = location;
        this.date = date;
        this.img = img;
        this.contents = contents;
    }
}
