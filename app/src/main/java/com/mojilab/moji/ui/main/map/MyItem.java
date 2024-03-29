package com.mojilab.moji.ui.main.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle;
    private String id;
    private String mSnippet = null;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(double lat, double lng, String mTitle) {
        mPosition = new LatLng(lat, lng);
        this.mTitle = mTitle;
    }

    public MyItem(double lat, double lng, String title, String snippet, String id) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        this.id = id;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public String getId() { return id;}
}