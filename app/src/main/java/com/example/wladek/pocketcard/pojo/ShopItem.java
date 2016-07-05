package com.example.wladek.pocketcard.pojo;

/**
 * Created by wladek on 7/3/16.
 */
public class ShopItem {
    private String name;
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        return id.equals(shopItem.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
