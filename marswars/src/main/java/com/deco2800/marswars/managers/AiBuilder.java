package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.managers.AiManager.State;
import com.deco2800.marswars.managers.GameBlackBoard.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


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
		float[] xy = new float[] {(int)builder.getPosX(), (int)builder.getPosY()};
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		LOGGER.info("Rock count: " + rm.getRocks(team));
		// Build building
		BuildingType building = decideBuilding(team);
		if (building != null && rm.getRocks(team) > building.getCost()) {
			if(findloc(xy, building)) {
				BuildAction buildAction = new BuildAction(builder, building, xy[0], xy[1]);
				LOGGER.info("Ai - Build " + building);
				builder.setAction(buildAction);
			}
			// Finalise Build (includes payment, difficultyMultiplier and cancellation
			//	if not enough)
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
		int buildingNum = black.count(team, Field.BUILDINGS);
		LOGGER.info("Building count: " + buildingNum);
		// follow this build pattern
		if (buildingNum >20) {
			return null;
		}
		switch(buildingNum - 1){
		case 0:
			return BuildingType.BUNKER;
		case 1:
			return BuildingType.BARRACKS;
		case 2:
			return BuildingType.BUNKER;
		case 3:
			return BuildingType.TURRET;
		default:	// from then on choose based on state
			switch(state){
			case AGGRESSIVE:
				return BuildingType.BARRACKS;
			case DEFENSIVE:
				return BuildingType.TURRET;
			default:	//case ECONOMIC:
				return BuildingType.BUNKER;
			}
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
		LOGGER.info("find ai build location");
		int originalX = (int) xy[0];
		int originalY = (int) xy[1];
		int xbuildArea = (int)(GameManager.get().getWorld().getWidth()*.15f);
		int ybuildArea = (int)(GameManager.get().getWorld().getLength()*.15f);
		int worldXBoundary = (GameManager.get().getWorld().getWidth())+1;
		int worldYBoundary = (GameManager.get().getWorld().getLength()+1);
		int avoidInfinite = 0;
		int randx, randy, xModified;
		do {
			int lowx = 1 < originalX - xbuildArea ? (originalX - xbuildArea) : 1;
			int highx = worldXBoundary > (originalX + xbuildArea) ? (originalX + xbuildArea) : worldXBoundary;
			int lowy = 1 < originalY - ybuildArea ? (originalY - ybuildArea) : 1;
			int highy = worldYBoundary > originalY + ybuildArea ? originalY + ybuildArea : worldYBoundary;
			randx = ThreadLocalRandom.current().nextInt(lowx, highx);
			randy = ThreadLocalRandom.current().nextInt(lowy, highy);
			//This modifier 'centers' point to be checked
			xModified = randx - ((int)(type.getBuildSize()+1)/2);
			avoidInfinite ++;
		}  while(!GameManager.get().getWorld().checkValidPlace(type, xModified, randy, (type.getBuildSize()), 0) && avoidInfinite < 100);
		if (avoidInfinite == 100) {
			return false;
		}
		xy[0] = randx;
		xy[1] = randy;
		return true;
	}
	

}
