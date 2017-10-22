package com.deco2800.marswars.managers;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.managers.AiManager.State;
import com.deco2800.marswars.managers.GameBlackBoard.Field;


public class AiBuilder extends Manager {
	private static final Logger LOGGER = LoggerFactory.getLogger(AiBuilder.class);
	private Random rand = new Random();
	
	/**
	 * Sets an ai builder to build
	 * 
	 * @param builder the builder to build something if possible
	 */
	public void build(Astronaut builder){
		int team = builder.getOwner();
		float[] xy = new float[] {builder.getPosX(), builder.getPosY()};
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		LOGGER.info("start");
		LOGGER.info("Rock count: " + rm.getRocks(team));
		// Build building
		BuildingType building = decideBuilding(team);
		BuildAction buildAction = new BuildAction(builder, building, xy[0], xy[1]);
		if (buildAction.canAfford(team, rm)) {
			if(findloc(xy, building)) {
				return;
			}
			LOGGER.info("Ai - Build " + building);
			builder.setAction(new BuildAction(builder, building, xy[0], xy[1]));
			
			// Finalise Build (includes payment, difficultyMultiplier and cancellation
			//	if not enough)
			if (builder.getAction().isPresent()) {
				((BuildAction)(builder.getAction().get())).finaliseBuildAI();
			}
		}
	}
	
	/**
	 * decides what to build 
	 * 
	 * @param team the team building
	 * @return the type of building to build
	 */
	private BuildingType decideBuilding(int team) {
		AiManager ai = (AiManager) GameManager.get().getManager(AiManager.class);
		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		State state = ai.getState(team);
		int buildingNum = black.count(team, Field.BUILDINGS) % 5;
		// with 1-in-3 chance choose based on state
		if (rand.nextInt(3) == 0) {
			switch(state){
			case AGGRESSIVE:
				return BuildingType.BARRACKS;
			case DEFENSIVE:
				return BuildingType.TURRET;
			case ECONOMIC:
				return BuildingType.BUNKER;
			}
		}
		// otherwise follow this build pattern
		switch(buildingNum - 1){
		case 0:
			return BuildingType.BUNKER;
		case 1:
			return BuildingType.BARRACKS;
		case 2:
			return BuildingType.BUNKER;
		case 3:
			return BuildingType.TURRET;
		default:	//case 4:
			return BuildingType.BASE;
		}
	}
	
	/**
	 * finds a valid location to build, return true if no valid locations near by
	 * 
	 * @param xy the x and y to start looking for
	 * @param type the type of building being built
	 * @return true if no location can be found
	 */
	private boolean findloc(float[] xy, BuildingType type) {
		LOGGER.info("find");
		int x = 0;
		int i = 0;
		while(!(GameManager.get().getWorld().checkValidPlace(type, xy[0], xy[1], (type.getBuildSize()), (float)0))) {
			if(x == 0) {
				x = 1;
				xy[0] = (float) (xy[0] + 1.0);
			}
			else {
				x = 0;
				xy[1] = (float) (xy[1] + 1.0);
			}
			i++;
			if(i > 50) {
				return true;
			}
		}
		return false;
	}
	

}
