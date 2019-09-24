package com.mojilab.moji.data;

import java.util.ArrayList;

public class OrderData {
    public int id;
    public int order;
    public String location;
    public String date;

    public OrderData(int id, int order, String location, String date){
        this.id = id;
        this.order = order;
        this.location = location;
        this.date = date;
    }
}
