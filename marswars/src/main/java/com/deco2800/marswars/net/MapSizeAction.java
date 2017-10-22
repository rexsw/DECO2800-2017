package com.deco2800.marswars.net;

import com.deco2800.marswars.worlds.MapSizeTypes;

;

/**
 * An action to send the client selected mapSize to the server.
 * 
 * @author James McCall
 *
 */
public class MapSizeAction implements Action {
    private MapSizeTypes size;
    
    public MapSizeAction() {
        // Blank constructor for Kryonet
    }
    
    public MapSizeAction(MapSizeTypes mapSize) {
        this.size = mapSize;
    }
    
    /**
     * 
     * @return the map size associated with this action.
     */
    public MapSizeTypes getMapSize() {
        return this.size;
    }
    
    @Override
    public String toString() {
        return "Request to change map size to: " + size.toString();
    }

}
