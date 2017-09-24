package com.deco2800.marswars.net;

import com.deco2800.marswars.worlds.MapSizeTypes;;

public class MapSizeAction implements Action {
    private MapSizeTypes size;
    
    /**
     * Blank constructor for Kryonet
     */
    public MapSizeAction() {
        
    }
    
    public MapSizeAction(MapSizeTypes mapSize) {
        this.size = mapSize;
    }
    
    public MapSizeTypes getMapSize() {
        return this.size;
    }
    
    @Override
    public String toString() {
        return "Request to change map size to: " + size.toString();
    }

}
