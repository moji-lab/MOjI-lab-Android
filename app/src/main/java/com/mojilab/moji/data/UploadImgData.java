package com.mojilab.moji.data;

import java.io.File;

public class UploadImgData {
    public int id;
    public boolean lock;
    public boolean boss;
    public String image;

    public UploadImgData(int id, Boolean lock, Boolean boss, String image){
        this.id = id;
        this.lock = lock;
        this.boss = boss;
        this.image = image;
    }
}
