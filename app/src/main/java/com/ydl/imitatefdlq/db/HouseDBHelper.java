package com.ydl.imitatefdlq.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qweenhool on 2017/8/16.
 */

public class HouseDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_HOUSE = "create table house ("
            + "id integer primary key autoincrement,"
            + "name varchar(80),"
            + "type varchar(40),"
            + "photo text,"
            + "account text,"
            + "room_number varchar(40))";

    public HouseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HOUSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
