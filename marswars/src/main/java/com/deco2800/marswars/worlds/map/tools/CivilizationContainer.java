package com.deco2800.marswars.worlds.map.tools;

import com.deco2800.marswars.worlds.CivilizationTypes;

import java.util.Random;


/**
 * Creates a container of a map's dimensions where according to an algorithm it can
 * decide where to place a full civilization which includes enemies, animals, structures, etc.
 */
public class CivilizationContainer {

    // randomizer
    private Random r = new Random();

    /**
     * Constructor of the civilization container where a civilization type is given.
     *
     * @param mapContainer the map that will contain the civilization
     * @param civilizationTypes the civilization to be use
     */
    public CivilizationContainer(MapContainer mapContainer, CivilizationTypes civilizationTypes) {
    	//Not yet implemented, waiting for further input element from other team.
    }

    /** Constructor of a civilization container of a random civilization.
     * @param mapContainer the map that will contain the civilization
     */
    public CivilizationContainer(MapContainer mapContainer) {
        //Not yet implemented 
    	CivilizationTypes random = CivilizationTypes.values()[r.nextInt(CivilizationTypes.values().length)];
    }
}
