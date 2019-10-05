package com.mojilab.moji.data;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;

public class PhotosData {
    public MultipartBody.Part photo;
    public boolean represent;

    public PhotosData(MultipartBody.Part photo, boolean represent){
        this.photo = photo;
        this.represent = represent;
    }
}
