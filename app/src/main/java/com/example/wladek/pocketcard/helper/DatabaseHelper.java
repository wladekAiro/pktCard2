package com.example.wladek.pocketcard.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.wladek.pocketcard.pojo.ShopItem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public boolean insertItems(String itemName , String itemCord , Double unitPrice){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEM_NAME", itemName);
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_ITEMS, null);

        return res;
    }


    public void insertIntoCart(ShopItem shopItem){

        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("ITEM CORD" , "====" + shopItem.getCode());

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE ITEM_CORD =?", new String[]{shopItem.getCode()});

        if (cursor.getCount() <= 0){
            Log.d("ITEM " , " == NOT FOUND ==" + shopItem.getCode());
        }else {
            Log.d("ITEM " , " == FOUND ==" + shopItem.getCode());
        }

        int tmpQnty  = 0;
        Double totalValue = new Double(0);

        if (cursor.getCount() == 0) {

            ContentValues contentValues = new ContentValues();
            contentValues.put("ITEM_NAME", shopItem.getName());
            contentValues.put("ITEM_CORD", shopItem.getCode());
            contentValues.put("UNIT_PRICE", shopItem.getUnitPrice());
            contentValues.put("ITEM_QTY", 1);
            contentValues.put("TOTAL_VALUE", shopItem.getUnitPrice());

            Long result = db.insert(TABLE_CART, null, contentValues);

            if (result == -1) {

                Log.d("ERROR INSERT ", " QUERY FAILED ON " + TABLE_CART);

            } else {

                Log.d("SUCCESS INSERT ", " QUERY SUCCESS ON " + TABLE_CART);
            }
        }else {

            if (cursor.moveToFirst()){

                do {
                    tmpQnty = cursor.getInt(4)+1;
                    totalValue = shopItem.getUnitPrice()*tmpQnty;
                }while (cursor.moveToNext());
            }

            if (tmpQnty < 6){

                ContentValues contentValues = new ContentValues();
                contentValues.put("ITEM_QTY", tmpQnty);
                contentValues.put("TOTAL_VALUE", totalValue);

                int result = db.update(TABLE_CART , contentValues ,"ITEM_CORD='"+shopItem.getCode()+"'" +
                        " AND ITEM_NAME='"+shopItem.getName()+"'" , null);

                Log.d(" UPDATE +++++++ ", " RESULT" + result);
            }

        }
    }

    public List<ShopItem> getCartItems(){

        List<ShopItem> shopItems = new ArrayList<ShopItem>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        if (res.getCount() > 0){
            while (res.moveToNext()) {

                ShopItem shopItem = new ShopItem();
                shopItem.setName(res.getString(1));
                shopItem.setCode(res.getString(2));
                shopItem.setUnitPrice(res.getDouble(3));
                shopItem.setCartQuantity(res.getInt(4));
                shopItem.setTotalCartValue(res.getDouble(5));

                shopItems.add(shopItem);
            }
        }

        return shopItems;
    }
}
