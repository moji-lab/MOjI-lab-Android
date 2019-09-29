package com.mojilab.moji.util.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

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
                    "idx INTEGER)";

    private static final String PHOTO =
            "CREATE TABLE IF NOT EXISTS photo (_id INTEGER PRIMARY KEY, " +
                    "photo TEXT, " +
                    "represent BOOLEAN )";

    //파라미터를 받는 생성자
    public DatabaseHelper(Context context) {
        super(context, "course", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COURSE);
        db.execSQL(TAG);
        db.execSQL(PHOTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }
}
