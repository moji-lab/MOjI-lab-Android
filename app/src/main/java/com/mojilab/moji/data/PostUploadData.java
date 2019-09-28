package com.mojilab.moji.data;

import java.util.ArrayList;

public class PostUploadData {
    public InfoData info;
    public ArrayList<CourseUploadData> course;

    public PostUploadData(InfoData info, ArrayList<CourseUploadData> course){
        this.info = info;
        this.course = course;
    }

}
