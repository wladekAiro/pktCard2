package com.example.wladek.pocketcard.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wladek on 10/16/16.
 */
public class CheckOutData implements Serializable{
    @SerializedName("cardNumber")
    private String cardNumber;
    @SerializedName("studentPin")
    private String studentPin;
    @SerializedName("shopItems")
    private ArrayList<ShopItem> shopItems;

    public String getStudentPin() {
        return studentPin;
    }

    public void setStudentPin(String studentPin) {
        this.studentPin = studentPin;
    }

    public ArrayList<ShopItem> getShopItems() {
        return shopItems;
    }

    public void setShopItems(ArrayList<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
