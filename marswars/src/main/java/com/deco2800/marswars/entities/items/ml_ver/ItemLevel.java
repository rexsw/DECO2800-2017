package com.deco2800.marswars.entities.items.ml_ver;

public enum ItemLevel {
    ONE(1, 200),
    TWO(2, 500),
    THREE(3, 1200);

    private int level;
    private final int xpNeeded;

    ItemLevel(int level, int xpNeeded) {
        this.level = level;
        this.xpNeeded = xpNeeded;
    }

    public int getLevel() { return level; }

    public int getXpNeeded() { return xpNeeded; }

    public void setLevel(int level) {
        this.level = level;
    }

}



