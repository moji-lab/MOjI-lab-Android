package com.mojilab.moji.data;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

public class PhotosData {
    public MultipartBody.Part photo;
    public RequestBody represent;

    public PhotosData(MultipartBody.Part photo, RequestBody represent){
        this.photo = photo;
        this.represent = represent;
    }
}
