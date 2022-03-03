package com.example.clothshop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactDb";

    public static final String TABLE_GOODS = "goods";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PRICE = "price";

    public static final String TABLE_ORDERS = "orders";
    public static final String KEY_ORDERID = "_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERADDRESS = "address";
    public static final String KEY_USERPHONE = "phone";

    public static final String TABLE_ORDERGOODS = "ordergoods";
    public static final String KEY_ORDERGOODID = "_id";
    public static final String KEY_OORDERID = "orderid";
    public static final String KEY_GOODID = "goodid";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_GOODS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_PRICE + " text" + ")");
        db.execSQL("create table " + TABLE_ORDERS + "(" + KEY_ORDERID
                + " integer primary key," + KEY_USERNAME + " text," + KEY_USERADDRESS + " text," + KEY_USERPHONE + " text" + ")");
        db.execSQL("create table " + TABLE_ORDERGOODS + "(" + KEY_ORDERGOODID
                + " integer primary key," + KEY_OORDERID + " text," + KEY_GOODID + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_GOODS);
        db.execSQL("drop table if exists " + TABLE_ORDERS);
        db.execSQL("drop table if exists " + TABLE_ORDERGOODS);

        onCreate(db);

    }
}
