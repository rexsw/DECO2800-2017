package com.deco2800.marswars.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.TerrainElements.ResourceType;

public class GameBlackBoard extends Manager implements TickableManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameBlackBoard.class);
	private List<Integer> teams = new ArrayList<Integer>();
	private Map<Integer,Map<String, Integer>> values = new HashMap<Integer,Map<String, Integer>>();
	private ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
	private int alive;
	
	/**
	 * testing use please keep for now 
	 */
	@Override
	public void onTick(long i) {
//		List<Manager> deepcopy = new ArrayList<Manager>((List<Manager>) GameManager.get().getManagerList());
//		Iterator<Manager> managersIter =  deepcopy.iterator();
//		while(managersIter.hasNext()) {
//			Manager m = managersIter.next();
//			if (m instanceof AbstractPlayerManager) {
//				if (m instanceof PlayerManager) {
//					ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
//					LOGGER.info("" + m.toString()+" has " + rm.getRocks() + " rocks");
//				} else if (m instanceof AiManagerTest) {
//					LOGGER.info("" + m.toString() +" has "+ ((AiManagerTest) m).getResources().getRocks() + " rocks");
//				}
//			}
//		}	
	}
	
	public void set() {
		values = new HashMap<Integer,Map<String, Integer>>();
		int teamid;
		for(BaseEntity e : GameManager.get().getWorld().getEntities()) {
			if(e instanceof HasOwner) {
				teamid = ((HasOwner) e).getOwner();
				if(values.containsKey(teamid)) {
					updateunit(teamid, e);
				}
				else {
					HashMap<String, Integer> teammap = new HashMap<String, Integer>();
					Set(teammap, teamid);
					updateunit(teamid, e);
				}
			}
		}
	}
	
	private void Set(HashMap<String, Integer> setmap, int teamid) {
		setmap.put("Biomass", 0);
		setmap.put("Crystal", 0);
		setmap.put("Rock", 0);
		setmap.put("Water", 0);
		setmap.put("Units", 0);
		setmap.put("Units Lost", 0);
		values.put(teamid, setmap);
		teams.add(teamid);
	}
	
	public void updateunit(int teamid, BaseEntity enity) {
		int count = values.get(teamid).get("Units");
		count++;
		values.get(teamid).put("Units", count);
	}
	
	public int unitcount(int teamid) {
		return values.get(teamid).get("Units");
	}
	
	public void updateDead(int teamid, BaseEntity enity) {
		int count = values.get(teamid).get("Units");
		int dead = values.get(teamid).get("Units Lost");
		count--;
		dead++;
		values.get(teamid).put("Units", count);
		values.get(teamid).put("Units Lost", dead);
	}
	
	public int deadcount(int teamid, BaseEntity enity) {
		return values.get(teamid).get("Units Lost");
	}
	
	public void updateResource(int teamid, ResourceType Resource) {
		switch(Resource){
			case WATER:
				values.get(teamid).put("Water", rm.getWater(teamid));
			case ROCK:
				values.get(teamid).put("Rock", rm.getRocks(teamid));
			case CRYSTAL:
				values.get(teamid).put("Crystal", rm.getCrystal(teamid));
			case BIOMASS:
				values.get(teamid).put("Biomass", rm.getBiomass(teamid));
		}
	}
	
	public int teamsAlive() {
		int count = 0;
		for(int t: values.keySet()) {
			if(values.get(t).get("Units") != 0) {
				alive = t;
				count++;
			}
		}
		return count;
	}
	
	public int getAlive() {
		return alive;
	}
	

}
