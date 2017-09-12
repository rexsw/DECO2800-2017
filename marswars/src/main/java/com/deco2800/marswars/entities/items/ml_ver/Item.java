package com.deco2800.marswars.entities.items.ml_ver;
import java.util.HashMap;

import com.deco2800.marswars.entities.items.ml_ver.EffectStats;
import com.deco2800.marswars.entities.items.ml_ver.ItemMetaData;

public class Item {

    private String name;
    private String itemType;
    private HashMap<EffectStats, Float> stats;

    public Item(ItemMetaData data) {
        this.name = data.getName();
        this.stats = data.getStats();
    }

    public String getName() {
        return this.name;
    }
    
    public HashMap<EffectStats, Float>  getStats() {
        return this.stats;
    }

    public void setItemType(String type) { this.itemType = type; }

    public String getItemType() { return itemType; }
}
