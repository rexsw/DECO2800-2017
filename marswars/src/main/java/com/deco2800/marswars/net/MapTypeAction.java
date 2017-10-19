package com.deco2800.marswars.net;

import com.deco2800.marswars.worlds.map.tools.MapTypes;

/**
 * Action for sending the client selected MapType to the server.
 * 
 * @author James McCall
 *
 */
public class MapTypeAction implements Action {
    // The mapType to send
    private MapTypes map;

    public MapTypeAction() {
        // balnk constructor for kryonet
    }
    
    public MapTypeAction(MapTypes mapType) {
        this.map = mapType;
    }
    
    /**
     * 
     * @return the map type corresponding to this action
     */
    public MapTypes getMapType() {
        return this.map;
    }
    
    @Override
    public String toString() {
        return "Change of map requested: " + map.toString();
    }
    
}
