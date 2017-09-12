package com.deco2800.marswars.entities.items.ml_ver;

public class ActiveItem extends Item {

    private boolean activation;

    public ActiveItem(String name) {
        super(name);
        this.activation = false;
    }

    public void activateItem() {
        this.activation = true;
    }

    public void deactivateItem() {
        this.activation = false;
    }

    public boolean getActivation() {
        return this.activation;
    }
}
