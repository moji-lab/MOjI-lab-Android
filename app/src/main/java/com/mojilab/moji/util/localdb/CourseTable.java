package com.mojilab.moji.util.localdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import com.mojilab.moji.data.CourseData;
import com.mojilab.moji.data.PhotoPath;

import java.util.ArrayList;

public class CourseTable {

    public Context context;
    public DatabaseHelper helper;
    public SQLiteDatabase database;

    static final String COURSECOLUMN = "main_address, sub_address, visit_time, content, tag_info, _order, lat, log, photos, share";
    static final String PHOTOCOLUMN = "indexNum, photoPath, represent";

    public CourseTable(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public void insertData(CourseData courseData) {

        Log.e("insertData", "insertData호출");

        if (database != null) {
            String sql = "insert or replace into course(" + COURSECOLUMN + ") values(?,?,?,?,?,?,?,?,?,?)";

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

            Object[] params = {courseData.mainAddress, courseData.subAddress, courseData.visitTime, courseData.content, courseData.tag, courseData.order, courseData.lat, courseData.lng, imgStr, share};

            database.execSQL(sql, params);

            Log.e("insertData", share.toString());
            Log.e("insertData11", params.toString());
        }
    }


    public ArrayList<CourseData> selectData() {

        if (database != null) {

             ArrayList<CourseData> courseDataArrayList = new ArrayList<>();

            String sql = "select _id, " + COURSECOLUMN + " from " + "course order by _order";

            Cursor cursor = database.rawQuery(sql, null);
            Log.e("조회된 데이터 개수 : ", String.valueOf(cursor.getCount()));

            if (cursor.getCount() == 0) {
                //Toast.makeText(context, "어플을 처음 실행 한 경우, 인터넷에 연결해야 데이터를 받아 올 수 있습니다.", Toast.LENGTH_SHORT).show();
            } else {
                for(int i = 0; i<cursor.getCount();i++){

                    CourseData courseData;

                    cursor.moveToNext();
                    Integer _id = cursor.getInt(0);
                    String main_address = cursor.getString(1);
                    String sub_address = cursor.getString(2);
                    String visit_time = cursor.getString(3);
                    String content = cursor.getString(4);
                    String tag_info = cursor.getString(5);
                    Integer _order = cursor.getInt(6);
                    Double lat = cursor.getDouble(7);
                    Double log = cursor.getDouble(8);
                    String photos = cursor.getString(9);
                    String shares = cursor.getString(10);

                    ArrayList<String> photo = new ArrayList<>();
                    ArrayList<Integer> share = new ArrayList<>();

                    String[] array = photos.split(", ");
                    String[] array0 = shares.split(", ");
                    for (int j = 0; j < array.length; j++) {
                        photo.add(array[j]);
                        share.add(Integer.valueOf(array0[j]));
                    }

                    courseData = new CourseData(_id,main_address, sub_address, visit_time, content, tag_info, _order, lat, log, photo, share);
                    courseDataArrayList.add(courseData);
                }
                return courseDataArrayList;
            }
        }
        return null;
    }

    public void updateOrderData(int id, int prev, int after){
        Log.e("안들어감....? id/prev/after","id:"+id+"prev"+prev+"after"+after);
        if (database != null) {

            Log.e("id/prev/after","id:"+id+"prev"+prev+"after"+after);

            String sql = "UPDATE course SET _order ='" + after + "' WHERE _order ='" +prev+ "'and _id = '"+ id+"';";
            Log.e("confirm-sql",sql);

            Cursor cursor = database.rawQuery(sql, null);
            cursor.getCount();
            Log.e("조회된 데이터 개수??!! : ", String.valueOf(cursor.getCount()));

        }
    }

    public int getCount() {

        if (database != null) {

            String sql = "select " + COURSECOLUMN + " from course";

            Cursor cursor = database.rawQuery(sql, null);
            Log.e("조회된 데이터 개수 : ", String.valueOf(cursor.getCount()));

            return cursor.getCount();

        }
        return 0;
    }


}
