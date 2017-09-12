package com.deco2800.marswars.entities.items.ml_ver;
import java.util.HashMap;
import java.util.Map;

import com.deco2800.marswars.entities.items.effects.EffectStats;

import java.util.AbstractMap;


public enum ItemMetaData {
	WEAPON1("Weapon 1", mapOf(entry(EffectStats.DMG, new Float(50)), entry(EffectStats.ATK_SPD, new Float(20))));
	
	private final String name;
	private final HashMap<EffectStats, Float> stats;
	
	ItemMetaData(String name, HashMap<EffectStats, Float> stats) {
		this.name = name;
		this.stats = stats;
	}
	
	public String getName() {
		return this.name;
	}
	
	public HashMap<EffectStats, Float> getStats() {
		return this.stats;
	}
	/*
	 * below gotten from https://stackoverflow.com/questions/507602/how-can-i-initialise-a-static-map/35662460#35662460
	 */
	@SafeVarargs
    private static HashMap<EffectStats, Float> mapOf(Map.Entry<EffectStats, Float>... entries) {
        HashMap<EffectStats, Float> map = new HashMap<EffectStats, Float>();
        for (Map.Entry<EffectStats, Float> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
    // Creates a map entry
    private static Map.Entry<EffectStats, Float> entry(EffectStats stat, Float value) {
        return new AbstractMap.SimpleEntry<EffectStats, Float>(stat, value);
    }

}