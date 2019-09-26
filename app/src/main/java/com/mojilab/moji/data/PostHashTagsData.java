package com.mojilab.moji.data;

import java.util.ArrayList;

public class PostHashTagsData {
    public String courseIdx;
    ArrayList<HashTagData> hashtags;

    public PostHashTagsData(String courseIdx, ArrayList<HashTagData> hashTagData){
        this.courseIdx = courseIdx;
        this.hashtags = hashTagData;
    }
}
