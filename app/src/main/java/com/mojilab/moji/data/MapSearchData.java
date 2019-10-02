package com.mojilab.moji.data;

public class MapSearchData{

    public String id;
    public String img;
    public String mainAddress;
    public String subAddress;
    public float lat;
    public float log;
    public int likeCnt;
    public boolean isLike;
    public String boardIdx;

    public MapSearchData(){
        this.id = id;
        this.img = img;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.lat = lat;
        this.log = log;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.boardIdx = boardIdx;
    }

    public MapSearchData(String id, String img, String mainAddress, String subAddress, float lat, float log, int likeCnt, Boolean isLike, String boardIdx){
        this.id = id;
        this.img = img;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.lat = lat;
        this.log = log;
        this.likeCnt = likeCnt;
        this.isLike = isLike;
        this.boardIdx = boardIdx;
    }
}