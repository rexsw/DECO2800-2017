package com.deco2800.marswars.entities;

import java.util.ArrayList;

import com.deco2800.marswars.entities.items.Armour;
import com.deco2800.marswars.entities.items.Item;
import com.deco2800.marswars.entities.items.Weapon;

public class Inventory {

    private Armour armour;
    private Weapon weapon;
    private ArrayList<Item> specials;

    public Inventory() {
        this.armour = null;
        this.weapon = null;
        this.specials = new ArrayList<Item>();
    }

    public void addToInventory(Item item) {
        switch (item.getItemType()) {
            case "Weapon":
                if (weapon == null) {
                    this.weapon = (Weapon) item;
                }
            case "Armour":
                if (armour == null) {
                    this.armour = (Armour) item;
                }
            case "Special":
                this.specials.add(item);
            default:
        }
    }

    public boolean removeFromInventory(Item item) {
        switch (item.getItemType()) {
            case "Weapon":
                if (weapon != null) {
                    this.weapon = null;
                    return true;
                }
            case "Armour":
                if (armour != null) {
                    this.armour = null;
                    return true;
                }
            case "Special":
                this.specials.remove(item);
            default: return false;
        }
    }

}
