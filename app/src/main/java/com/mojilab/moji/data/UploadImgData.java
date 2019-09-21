package com.mojilab.moji.data;

import android.net.Uri;

import java.util.ArrayList;

public class UploadImgData {
    public int id;
    public boolean lock;
    public boolean boss;
    public Uri image;

    public UploadImgData(int id, Boolean lock, Boolean boss, Uri image){
        this.id = id;
        this.lock = lock;
        this.boss = boss;
        this.image = image;
    }
}
