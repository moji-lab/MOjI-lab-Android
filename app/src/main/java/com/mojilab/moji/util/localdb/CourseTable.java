package com.mojilab.moji.util.localdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import com.mojilab.moji.data.CourseData;

public class CourseTable {

    public Context context;
    public DatabaseHelper helper;
    public SQLiteDatabase database;

    static final String COURSECOLUMN = "main_address, sub_address, visit_time, content, _order, lat, log, photos, share";

    public CourseTable(Context context){
        this.context = context;
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public void insertData(CourseData courseData) {

        Log.e("insertData", "insertData호출");

        if (database != null) {
            String sql = "insert or replace into course("+COURSECOLUMN+") values(?,?,?,?,?,?,?,?,?)";

            String imgStr = "";
            String share = "";

            Log.e("cnt", String.valueOf(courseData.photos.size()));

            for(int i = 0; i<courseData.photos.size();i++){
                if(i != 0){
                    imgStr +=", ";
                    share +=", ";
                }
                imgStr +=courseData.photos.get(i);
                share +=courseData.share.get(i)+"";
                Log.e("share, photo",share + "," +imgStr);
            }

            Object[] params = {courseData.mainAddress, courseData.subAddress, courseData.visitTime, courseData.content, courseData.order, courseData.lat, courseData.log, imgStr, share};

            database.execSQL(sql, params);

            Log.e("insertData", share.toString());
            Log.e("insertData11", params.toString());
        }
    }

}
