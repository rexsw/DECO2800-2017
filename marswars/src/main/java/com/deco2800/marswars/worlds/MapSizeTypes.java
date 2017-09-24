package com.deco2800.marswars.worlds;

/**
 * Maps (.tmx files) different sizes
 */
public enum MapSizeTypes {
    TINY, SMALL, MEDIUM, LARGE, VERY_LARGE;
    
    @Override
    public String toString() {
        String result = this.name();
        result.toLowerCase();
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }
}
