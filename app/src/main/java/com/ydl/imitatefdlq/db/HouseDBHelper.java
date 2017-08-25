package com.ydl.imitatefdlq.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qweenhool on 2017/8/16.
 */

public class HouseDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_HOUSE = "create table house ("
            + "_id integer primary key autoincrement,"
            + "name varchar(80),"
            + "type varchar(40),"
            + "photo text,"
            + "account text,"
            + "room_number varchar(40))";

    private static final String CREATE_HOUSE_BAK = "create table House_bak ("
            + "id text primary key,"
            + "data_upload integer,"
            + "house_name text,"
            + "order_number integer,"//new Date()
            + "house_type integer,"
            + "use_fee_template integer)";

    private static final String CREATE_PICTURE = "create table Picture ("
            + "id text primary key,"
            + "path text,"
            + "foreign_id text,"
            + "order_num integer,"
            + "data_upload integer,"
            + "upload_url text"
            + "sort_No integer)";

    private static final String CREATE_ROOM = "create table Room ("
            + "id text primary key,"
            + "data_upload integer,"
            + "order_num integer,"
            + "house_id text,"
            + "room_name text,"
            + "create_time text)";

    public HouseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HOUSE);
        db.execSQL(CREATE_PICTURE);
        db.execSQL(CREATE_HOUSE_BAK);
        db.execSQL(CREATE_ROOM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
