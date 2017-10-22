package com.deco2800.marswars.entities;


/**
 * A list of all entities that are considered Units and Spacman
 *
 * NOTE: This list shouldn't include entities such as buildings or resources.
 */
/*A more suitable name will be appreciated*/
public enum EntityID {
    ASTRONAUT(0, 0, 20),
    COMMANDER(50, 50, 50),
    CARRIER(20, 20, 0),
    MEDIC(0, 15, 40),
    SOLDIER(0, 25, 10),
    TANK(50, 40, 0),
    SPACMAN(0, 10, 15),
    HACKER(10, 50, 10),
    SNIPER(0, 50, 5),
    TANKDESTROYER(30, 30, 0),
    SPATMAN(0, 15, 10);
	
	private final int entityCostRocks;
	private final int entityCostCrystals;
	private final int entityCostBiomass;
	
	EntityID(int entityCostRocks, int entityCostCrystals, int entityCostBiomass) {
        this.entityCostRocks = entityCostRocks;
        this.entityCostCrystals = entityCostCrystals;
        this.entityCostBiomass = entityCostBiomass;
    }
    public int getCostRocks() {
    	return entityCostRocks;
    }
    public int getCostCrystals() {
    	return entityCostCrystals;
    }
    public int getCostBiomass() {
    	return entityCostBiomass;
    }

}

// Add me new types