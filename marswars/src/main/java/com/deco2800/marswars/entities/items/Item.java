package com.deco2800.marswars.entities.items;

public class Item {

    private String name;
    private String itemType;

    public Item(String name) {
        this.name = name;
    }

    public String getName(Item item) {
        return this.name;
    }

    public void setItemType(String type) { this.itemType = type; }

    public String getItemType() { return itemType; }
}
