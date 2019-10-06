package com.mojilab.moji.data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Info {

    @SerializedName("open")
    @Expose
    private boolean open;
    @SerializedName("mainAddress")
    @Expose
    private String mainAddress;
    @SerializedName("subAddress")
    @Expose
    private String subAddress;
    @SerializedName("share")
    @Expose
    private ArrayList<Integer> share;

    public Info(boolean open, String mainAddress, String subAddress, ArrayList<Integer> share) {
        this.open = open;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.share = share;
    }

    // other constructors, getter and setter methods
}