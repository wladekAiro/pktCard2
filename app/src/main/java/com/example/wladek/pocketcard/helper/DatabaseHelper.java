package com.example.wladek.pocketcard.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wladek.pocketcard.pojo.SchoolDetails;
import com.example.wladek.pocketcard.pojo.ShopItem;
import com.example.wladek.pocketcard.pojo.StudentData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wladek on 7/5/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pocketcard.db";
    public static final String TABLE_ITEMS = "tbl_items";
    public static final String TABLE_SCHOOLDETAILS = "tbl_schooldetails";
    public static final String TABLE_CART = "tbl_cart";
    public static final String TABLE_STUDENT = "tbl_student";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_NAME TEXT, " +
                "ITEM_CORD TEXT, UNIT_PRICE DOUBLE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CART + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_NAME TEXT, ITEM_CORD TEXT," +
                " UNIT_PRICE DOUBLE , ITEM_QTY INTEGER ,TOTAL_VALUE DOUBLE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_STUDENT + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, STUDENT_NAME TEXT, STUDENT_NUMBER TEXT, CARD_NUMBER TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SCHOOLDETAILS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SCHOOL_NAME TEXT, SCHOOL_CODE TEXT , LOGGED_IN INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHOOLDETAILS);
        onCreate(db);
    }

    public boolean insertItems(String itemName , String itemCord , Double unitPrice){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE ITEM_CORD =?", new String[]{itemCord});
        ContentValues contentValues = null;
        if (cursor.getCount() == 0){
            contentValues = new ContentValues();
            contentValues.put("ITEM_NAME", itemName);
            contentValues.put("ITEM_CORD", itemCord);
            contentValues.put("UNIT_PRICE", unitPrice);

            Long result =  db.insert(TABLE_ITEMS, null, contentValues);

            if(result == -1){
                cursor.close();
                db.close();
                return false;
            }else {
                cursor.close();
                db.close();
                return  true;
            }

        }else {
            contentValues = new ContentValues();
            contentValues.put("ITEM_NAME", itemName);
            contentValues.put("ITEM_CORD", itemCord);
            contentValues.put("UNIT_PRICE", unitPrice);
            int result = db.update(TABLE_CART, contentValues, "ITEM_CORD='" + itemCord + "'", null);

            if (result >= 1){
                cursor.close();
                db.close();
                return  true;
            }else {
                cursor.close();
                db.close();
                return false;
            }
        }

    }

    public ArrayList<ShopItem> getAllShopItems(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_ITEMS, null);
        ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();

        if (res.getCount() > 0) {
            while (res.moveToNext()) {

                ShopItem shopItem = new ShopItem();
                shopItem.setName(res.getString(1));
                shopItem.setCode(res.getString(2));
                shopItem.setUnitPrice(res.getDouble(3));

                shopItems.add(shopItem);
            }
        }

        res.close();
        db.close();

        return shopItems;
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

        cursor.close();
        db.close();
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

        res.close();
        db.close();

        return shopItems;
    }

    public int updateCart(int quantity , ShopItem shopItem){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEM_QTY", quantity);
        contentValues.put("TOTAL_VALUE", (shopItem.getUnitPrice() * quantity));

        int result = db.update(TABLE_CART, contentValues, "ITEM_CORD='" + shopItem.getCode() + "'" +
                " AND ITEM_NAME='" + shopItem.getName() + "'", null);

        db.close();

        return result;
    }

    public void removeFromCart(ShopItem shopItem){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_CART , "ITEM_CORD =?", new String[]{shopItem.getCode()} );
        db.close();
    }

    public boolean insertSchoolDetails(SchoolDetails schoolDetails , int loggedIn){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_SCHOOLDETAILS, null);

        if (res.getCount() > 1){
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("SCHOOL_NAME", schoolDetails.getSchoolName());
        contentValues.put("SCHOOL_CODE", schoolDetails.getSchoolCode());
        contentValues.put("LOGGED_IN", loggedIn);

        Long result =  db.insert(TABLE_SCHOOLDETAILS, null, contentValues);

        if(result == -1){
            db.close();
            return false;
        }else {
            db.close();
            return  true;
        }

    }

    public SchoolDetails getSchoolDetails(){

        SQLiteDatabase db = this.getReadableDatabase();

        SchoolDetails schoolDetails = null;

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_SCHOOLDETAILS, null);

        if (res.getCount() > 0){

            schoolDetails = new SchoolDetails();
            int loggedIn = 0;

            while (res.moveToNext()) {

                schoolDetails.setSchoolName(res.getString(1));
                schoolDetails.setSchoolCode(res.getString(2));
                loggedIn = res.getInt(3);

            }

            if (loggedIn == 1){
                schoolDetails.setLoggedIn(true);
            }else{
                schoolDetails.setLoggedIn(false);
            }
        }

        res.close();
        db.close();

        return schoolDetails;
    }

    public StudentData getStudentData() {
        SQLiteDatabase db = this.getReadableDatabase();

        StudentData studentData = null;

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_STUDENT, null);

        if (res.getCount() > 0){

            studentData = new StudentData();

            while (res.moveToNext()) {

                studentData.setStudentName(res.getString(1));
                studentData.setStudentNumber(res.getString(2));
                studentData.setCardNumber(res.getString(3));

            }

            db.close();

            return studentData;
        }

        res.close();
        db.close();

        return null;
    }

    public void clearCart(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART , null , null );
        db.close();
    }
}
