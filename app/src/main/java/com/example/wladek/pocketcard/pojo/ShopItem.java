package com.example.wladek.pocketcard.pojo;

import java.io.Serializable;

/**
 * Created by wladek on 7/3/16.
 */
public class ShopItem implements Serializable{
    private Long id;
    private String name;
    private String code;
    private int cartQuantity;
    private Double unitPrice;
    private Double totalCartValue;

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

    public Double getTotalCartValue() {
        return totalCartValue;
    }

    public void setTotalCartValue(Double totalCartValue) {
        this.totalCartValue = totalCartValue;
    }

    public int getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
