package com.mojilab.moji.data;

import java.util.ArrayList;

public class PostUploadData {
    public InfoData info;
    public ArrayList<CourseUploadData> courses;

    public PostUploadData(InfoData info, ArrayList<CourseUploadData> courses){
        this.info = info;
        this.courses = courses;
    }

}
