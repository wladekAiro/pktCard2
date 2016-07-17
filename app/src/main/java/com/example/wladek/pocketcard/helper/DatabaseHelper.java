package com.example.wladek.pocketcard.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wladek.pocketcard.pojo.ShopItem;

import java.math.BigInteger;

/**
 * Created by wladek on 7/5/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pocketcard.db";
    public static final String TABLE_ITEMS = "tbl_items";
    public static final String TABLE_CART = "tbl_cart";
    public static final String TABLE_SUDENTS = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "CORD";
    public static final String COL4 = "UNIT_PRICE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_NAME TEXT, " +
                "ITEM_CORD TEXT, UNIT_PRICE DOUBLE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CART + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_NAME TEXT, ITEM_CORD TEXT," +
                " UNIT_PRICE DOUBLE , ITEM_QTY INTEGER ,TOTAL_VALUE DOUBLE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SUDENTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, STUDENT_NAME TEXT, STUDENT_NUMBER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SUDENTS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CART);
        onCreate(db);
    }

    public boolean insertItems(String itemName , String itemCord , Double unitPrice){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEM_NAME" , itemName);
        contentValues.put("ITEM_CORD", itemCord);
        contentValues.put("UNIT_PRICE", unitPrice);

        Long result =  db.insert(TABLE_ITEMS, null, contentValues);

        if(result == -1){
            return false;
        }else {
            return  true;
        }

    }

    public Cursor getAllShopItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_ITEMS , null);

        return res;
    }
}
