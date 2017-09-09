package com.deco2800.marswars.entities.items;

public class Item {

    private String name;
    private Effect effect;

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, Effect effect) {
        this.name = name;
        this.effect = effect;
    }

    public String getName(Item item) {
        return this.name;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public void addEffect(Effect effect) {
        this.effect = effect;
    }
}
