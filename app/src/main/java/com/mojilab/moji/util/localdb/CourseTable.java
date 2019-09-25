package com.mojilab.moji.util.localdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import com.mojilab.moji.data.CourseData;

import java.util.ArrayList;

public class CourseTable {

    public Context context;
    public DatabaseHelper helper;
    public SQLiteDatabase database;

    static final String COURSECOLUMN = "main_address, sub_address, visit_time, content, _order, lat, log, photos, share";

    public CourseTable(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public void insertData(CourseData courseData) {

        Log.e("insertData", "insertData호출");

        if (database != null) {
            String sql = "insert or replace into course(" + COURSECOLUMN + ") values(?,?,?,?,?,?,?,?,?)";

            String imgStr = "";
            String share = "";

            Log.e("cnt", String.valueOf(courseData.photos.size()));

            for (int i = 0; i < courseData.photos.size(); i++) {
                if (i != 0) {
                    imgStr += ", ";
                    share += ", ";
                }
                imgStr += courseData.photos.get(i);
                share += courseData.share.get(i) + "";
                Log.e("share, photo", share + "," + imgStr);
            }

            Object[] params = {courseData.mainAddress, courseData.subAddress, courseData.visitTime, courseData.content, courseData.order, courseData.lat, courseData.log, imgStr, share};

            database.execSQL(sql, params);

            Log.e("insertData", share.toString());
            Log.e("insertData11", params.toString());
        }
    }

    public ArrayList<CourseData> selectData() {

        if (database != null) {

             ArrayList<CourseData> courseDataArrayList = new ArrayList<>();

            String sql = "select " + COURSECOLUMN + " from " + "course";

            Cursor cursor = database.rawQuery(sql, null);
            Log.e("조회된 데이터 개수 : ", String.valueOf(cursor.getCount()));

            if (cursor.getCount() == 0) {
                //Toast.makeText(context, "어플을 처음 실행 한 경우, 인터넷에 연결해야 데이터를 받아 올 수 있습니다.", Toast.LENGTH_SHORT).show();
            } else {
                for(int i = 0; i<cursor.getCount();i++){

                    CourseData courseData;

                    cursor.moveToNext();
                    String main_address = cursor.getString(0);
                    String sub_address = cursor.getString(1);
                    String visit_time = cursor.getString(2);
                    String content = cursor.getString(3);
                    Integer _order = cursor.getInt(4);
                    float lat = cursor.getFloat(5);
                    float log = cursor.getFloat(6);
                    String photos = cursor.getString(7);
                    String shares = cursor.getString(8);

                    ArrayList<String> photo = new ArrayList<>();
                    ArrayList<Integer> share = new ArrayList<>();

                    String[] array = photos.split(", ");
                    String[] array0 = shares.split(", ");
                    for (int j = 0; j < array.length; j++) {
                        photo.add(array[j]);
                        share.add(Integer.valueOf(array0[j]));
                    }

                    courseData = new CourseData(main_address, sub_address, visit_time, content, _order, lat, log, photo, share);
                    courseDataArrayList.add(courseData);
                    Log.e("selectData", "sss");
                }
                return courseDataArrayList;
            }
        }
        return null;
    }

    public int getCount() {

        if (database != null) {

            CourseData courseData;

            String sql = "select " + COURSECOLUMN + " from " + "course";

            Cursor cursor = database.rawQuery(sql, null);
            Log.e("조회된 데이터 개수 : ", String.valueOf(cursor.getCount()));

            return cursor.getCount();

        }
        return 0;
    }


}
