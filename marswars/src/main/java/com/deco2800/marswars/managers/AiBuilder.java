package com.deco2800.marswars.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.managers.AiManager.State;
import com.deco2800.marswars.managers.GameBlackBoard.Field;

public class AiBuilder extends Manager {
	private int choice = 0;
	private static final Logger LOGGER = LoggerFactory.getLogger(AiBuilder.class);
	
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
		if (rm.getRocks(team) > 50) {
			choice = whichbuild(team);
			switch(choice) {
			case 1:
				if(findloc(xy, BuildingType.BASE)) {
					return;
				}
				LOGGER.info("BUILD BASE");
				builder.setAction(new BuildAction(builder, BuildingType.BASE, xy[0], xy[1]));
				break;
			case 0:
				if(findloc(xy, BuildingType.BARRACKS)) {
					return;
				}
				builder.setAction(new BuildAction(builder, BuildingType.BARRACKS, xy[0], xy[1]));
				break;
			case 2:
				if(findloc(xy, BuildingType.BUNKER)) {
					return;
				}
				builder.setAction(new BuildAction(builder, BuildingType.BUNKER, xy[0], xy[1]));
				break;
			case 3:
				if(findloc(xy, BuildingType.TURRET)) {
					return;
				}
				builder.setAction(new BuildAction(builder, BuildingType.TURRET, xy[0], xy[1]));
				break;
			}
			if (builder.getAction().isPresent()) {
				((BuildAction)(builder.getAction().get())).finaliseBuild();
			}
			}
		}
	
	/**
	 * decides what to build
	 * 
	 * @param team the team building
	 * @return a int 0-3 that shows what to build 
	 */
	private int whichbuild(int team) {
		int ret = team;
		AiManager ai = (AiManager) GameManager.get().getManager(AiManager.class);
		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		State state = ai.getState(team);
		if(black.count(team, Field.BUILDINGS) > (black.count(team, Field.UNITS) / 2)) {
			ret *= 3;
		}
		if(black.count(team, Field.COMBAT_UNITS) < (black.count(team, Field.UNITS) / 2)) {
			ret *= 4;
		}
		switch(state){
			case AGGRESSIVE:
				ret *= 4;
			case DEFENSIVE:
				ret *= 3;
			case ECONOMIC:
				ret *= 1;
		}
		return ret % 4;
	}
	
	/**
	 * finds a vaild loction to build, return true if no vaild locations near by
	 * 
	 * @param xy the x and y to start looing for
	 * @param type the type of building being built
	 * @return true if no loction can be found
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
