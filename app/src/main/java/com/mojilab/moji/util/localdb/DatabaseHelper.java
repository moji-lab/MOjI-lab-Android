package com.mojilab.moji.util.localdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mojilab.moji.data.CourseTagData;
import com.mojilab.moji.data.PhotoPath;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String PHOTOCOLUMN = "indexNum, photoPath, represent";

    private static final String COURSE =
            "CREATE TABLE IF NOT EXISTS course (_id INTEGER PRIMARY KEY, " +
                    "main_address TEXT, " +
                    "sub_address TEXT, " +
                    "visit_time TEXT, " +
                    "content TEXT, " +
                    "tag_info TEXT, " +
                    "_order INTEGER, " +
                    "lat FLOAT, " +
                    "log FLOAT, " +
                    "photos TEXT, " +
                    "share TEXT )";

    private static final String TAG =
            "CREATE TABLE IF NOT EXISTS tag (_id INTEGER PRIMARY KEY, " +
                    "content TEXT)";

    private static final String PHOTOURL =
            "CREATE TABLE IF NOT EXISTS photourl (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "indexNum INT, " +
                    "photoPath TEXT, " +
                    "represent INT )";

    //파라미터를 받는 생성자
    public DatabaseHelper(Context context) {
        super(context, "course", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COURSE);
        db.execSQL(TAG);
        db.execSQL(PHOTOURL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }


    public void insertPhoto(PhotoPath photoPath){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO photourl VALUES(null, '" + photoPath.getIndexNum() + "', '" + photoPath.getPhotoPath() + "', '" + photoPath.getRepresent() + "');");
        db.close();
    }

    public void insertTag(CourseTagData courseTagData){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO tag VALUES(null, '" + courseTagData.getIndexNum() + "', '" + courseTagData.getContent() + "');");
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM course;");
        db.execSQL("DELETE FROM photourl;");
        db.execSQL("DELETE FROM tag;");
        db.close();
    }

    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM photourl", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + " : "
                    + cursor.getString(2)
                    + " : "
                    + cursor.getString(3)
                    + "\n";
        }

        return result;
    }

}
