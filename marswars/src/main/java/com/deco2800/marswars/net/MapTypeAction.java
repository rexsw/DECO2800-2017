package com.deco2800.marswars.net;

import com.deco2800.marswars.worlds.map.tools.MapTypes;

public class MapTypeAction implements Action {

    private MapTypes map;
    /**
     * Blank constructor for kryonet.
     */
    public MapTypeAction() {
        
    }
    
    public MapTypeAction(MapTypes mapType) {
        this.map = mapType;
    }
    
    public MapTypes getMapType() {
        return this.map;
    }
    
    @Override
    public String toString() {
        return "Change of map requested: " + map.toString();
    }
    
}
