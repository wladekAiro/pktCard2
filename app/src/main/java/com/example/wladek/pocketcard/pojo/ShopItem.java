package com.example.wladek.pocketcard.pojo;

import java.io.Serializable;

/**
 * Created by wladek on 7/3/16.
 */
public class ShopItem implements Serializable{
    private String name;
    private String code;
    private Double unitPrice;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShopItem shopItem = (ShopItem) o;

        return code.equals(shopItem.code);

    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
