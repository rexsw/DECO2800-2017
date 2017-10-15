package com.deco2800.marswars.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.buildings.Barracks;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.buildings.Bunker;
import com.deco2800.marswars.buildings.Turret;
import com.deco2800.marswars.entities.units.Astronaut;

public class AiBuilder extends Manager {
	private int choice = 0;
	private static final Logger LOGGER = LoggerFactory.getLogger(AiBuilder.class);
	
	public void build(Astronaut builder){
		int team = builder.getOwner();
		float xy[] = new float[] {builder.getPosX(), builder.getPosY()};
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		LOGGER.info("start");
		if(rm.getCrystal(team) > 50) {
			rm.setCrystal(rm.getCrystal(team) - 30, team);
			switch(choice) {
			case 0:
				if(findloc(xy, BuildingType.BASE)) {
					return;
				}
				builder.setAction(new BuildAction(builder, BuildingType.BASE, xy[0], xy[1]));
			case 1:
				if(findloc(xy, BuildingType.BARRACKS)) {
					return;
				}
				builder.setAction(new BuildAction(builder, BuildingType.BARRACKS, xy[0], xy[1]));
			case 2:
				if(findloc(xy, BuildingType.BUNKER)) {
					return;
				}
				builder.setAction(new BuildAction(builder, BuildingType.BUNKER, xy[0], xy[1]));
			case 3:
				if(findloc(xy, BuildingType.TURRET)) {
					return;
				}
				builder.setAction(new BuildAction(builder, BuildingType.TURRET, xy[0], xy[1]));
			}
			choice = choice + 1 % 4;
			}
		}
	
	private boolean findloc(float xy[], BuildingType type) {
		LOGGER.info("find");
		int x = 0;
		int i = 0;
		while(!GameManager.get().getWorld().checkValidPlace(type, xy[0], xy[1], (type.getBuildSize()), (float)0)) {
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
