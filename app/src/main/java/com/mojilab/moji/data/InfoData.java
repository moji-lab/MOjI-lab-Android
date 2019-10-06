package com.mojilab.moji.data;

import java.util.ArrayList;

public class InfoData {
    public Boolean open;
    public String mainAddress;
    public String subAddress;
    public ArrayList<Integer> share;

    public InfoData(Boolean open, String mainAddress, String subAddress, ArrayList<Integer> share){
        this.open = open;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.share = share;
    }
}
