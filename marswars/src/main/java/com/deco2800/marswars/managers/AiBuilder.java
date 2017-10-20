package com.deco2800.marswars.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.units.Astronaut;

public class AiBuilder extends Manager {
	private int choice = 0;
	private static final Logger LOGGER = LoggerFactory.getLogger(AiBuilder.class);
	
	public void build(Astronaut builder){
		int team = builder.getOwner();
		float xy[] = new float[] {builder.getPosX(), builder.getPosY()};
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		LOGGER.info("start");
		LOGGER.info("Crystal count: " + rm.getCrystal(team));
		if (rm.getRocks(team) > 50) {
			switch(choice) {
			case 0:
				if(findloc(xy, BuildingType.BASE)) {
					return;
				}
				LOGGER.info("BUILD BASE");
				builder.setAction(new BuildAction(builder, BuildingType.BASE, xy[0], xy[1]));
				break;
			case 1:
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
			choice = (choice + 1) % 4;
			if (builder.getAction().isPresent()) {
				((BuildAction)(builder.getAction().get())).finaliseBuild();
			}
			}
		}
	
	private boolean findloc(float xy[], BuildingType type) {
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
