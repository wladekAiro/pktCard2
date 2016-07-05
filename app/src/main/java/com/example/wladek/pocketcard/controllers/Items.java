package com.example.wladek.pocketcard.controllers;

import com.example.wladek.pocketcard.pojo.ShopItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wladek on 7/3/16.
 */
public class Items {

    public List<ShopItem> generate(){
        List<ShopItem> itemList = new ArrayList<ShopItem>();

        ShopItem shopItem = new ShopItem();
        shopItem.setId(new Long(1));
        shopItem.setName("Bread 1/4");
        itemList.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setId(new Long(2));
        shopItem.setName("Mandazi");
        itemList.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setId(new Long(3));
        shopItem.setName("Cup cake");
        itemList.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setId(new Long(4));
        shopItem.setName("Avacado");
        itemList.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setId(new Long(5));
        shopItem.setName("Pen");
        itemList.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setId(new Long(6));
        shopItem.setName("Pencil");
        itemList.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setId(new Long(7));
        shopItem.setName("Exercise book");
        itemList.add(shopItem);

        return itemList;
    }
}
