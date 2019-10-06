package com.mojilab.moji.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Attach {

    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("courses")
    @Expose
    private ArrayList<Courses> courses;

    public Attach(Info info, ArrayList<Courses> courses) {
        this.info = info;
        this.courses = courses;
    }

    // constructors, getter and setter methods
}