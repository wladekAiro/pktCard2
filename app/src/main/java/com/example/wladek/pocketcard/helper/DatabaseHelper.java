package com.example.wladek.pocketcard.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wladek on 7/5/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pocketcard.db";
    public static final String TABLE_ITEMS = "tbl_items";
    public static final String TABLE_SUDENTS = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "CORD";
    public static final String COL4 = "UNIT_PRICE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ITEMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_NAME TEXT, ITEM_CORD TEXT, UNIT PRICE INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_SUDENTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, STUDENT_NAME TEXT, STUDENT_NUMBER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABL IF EXISTS "+TABLE_ITEMS);
        db.execSQL("DROP TABL IF EXISTS "+TABLE_SUDENTS);
        onCreate(db);
    }
}
