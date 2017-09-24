package com.deco2800.marswars.worlds.map.tools;

/**
 * Map types available for building worlds
 */
public enum MapTypes {
    MARS, MOON, SUN;
    
    @Override
    public String toString() {
            String result = this.name();
            result.toLowerCase();
            result = result.substring(0, 1).toUpperCase() + result.substring(1);
            return result;
    }
}


// add me new types