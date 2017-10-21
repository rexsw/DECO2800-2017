package com.deco2800.marswars.buildings;

import com.deco2800.marswars.worlds.AbstractWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * This class creates a hero factory building that used to create hero characters
 * It may also act as a shop for hero in the later stage
 * NOTE: currently it does not affect the game
 *
 */
public class HeroFactory extends BuildingEntity {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(HeroFactory.class);

    /**
     * Constructor for the hero factory.
     * @param world The world that will hold the hero factory.
     * @param posX its x position on the world.
     * @param posY its y position on the world.
     * @param posZ its z position on the world.
     */
    public HeroFactory(AbstractWorld world, float posX, float posY, float
            posZ, int owner) {
        super(posX, posY, posZ, BuildingType.HEROFACTORY, owner);
        LOGGER.debug("Create a hero factory");
    }
}
