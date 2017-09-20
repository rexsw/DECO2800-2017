package com.deco2800.marswars.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.units.AttackableEntity;

/**
 * A class to track various things in the game and to keep a history of them
 * 
 * @author Scott Whittington
 *
 */
public class GameBlackBoard extends Manager implements TickableManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameBlackBoard.class);
	private List<Integer> teams = new ArrayList<Integer>();
	private Map<Integer,Map<String, List<Integer>>> values = new HashMap<Integer,Map<String, List<Integer>>>();
	private ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
	private int alive;
	private int timer;
	private int index = 0;
	

	@Override
	public void onTick(long i) {
		//adds to the history of each field every few ticks
		timer++;
		if(timer % 10 == 0) {
			//LOGGER.info("tick");
			for(int teamid: teams) {
				values.get(teamid).get("Biomass").add(rm.getBiomass(teamid));
				values.get(teamid).get("Crystal").add(rm.getCrystal(teamid));
				values.get(teamid).get("Rocks").add(rm.getRocks(teamid));
				values.get(teamid).get("Water").add(rm.getWater(teamid));
				values.get(teamid).get("Units").add(this.count(teamid, "Units"));
				values.get(teamid).get("Units Lost").add(this.count(teamid, "Units Lost"));
				values.get(teamid).get("Combat Units").add(this.count(teamid, "Combat Units"));
				values.get(teamid).get("Buildings").add(this.count(teamid, "Buildings"));
			}
			index++;
		}
	}
	
	/**
	 * sets the BlackBoard at the start of a game
	 * 
	 * @ensure called after the game world has been set up and is only called once
	 */
	public void set() {
		values = new HashMap<Integer,Map<String, List<Integer>>>();
		int teamid;
		for(BaseEntity e : GameManager.get().getWorld().getEntities()) {
			if(e instanceof HasOwner) {
				teamid = ((HasOwner) e).getOwner();
				if(values.containsKey(teamid)) {
					updateunit(e);
				}
				else {
					if(teamid != 0) {
						HashMap<String, List<Integer>> teammap = new HashMap<String, List<Integer>>();
						Set(teammap, teamid);
						updateunit(e);
					}
				}
			}
		}
	}
	
	/**
	 * a helper methoad to set a Map up for use in the BlackBaord
	 * 
	 * @param setmap map the map to be set up
	 * @param teamid int the teamid to map to
	 */
	private void Set(HashMap<String, List<Integer>> setmap, int teamid) {
		ArrayList<Integer> base = new ArrayList<Integer>();
		base.add(0);
		setmap.put("Biomass", new ArrayList<Integer>(base));
		setmap.put("Crystal",  new ArrayList<Integer>(base));
		setmap.put("Rocks",  new ArrayList<Integer>(base));
		setmap.put("Water",  new ArrayList<Integer>(base));
		setmap.put("Units",  new ArrayList<Integer>(base));
		setmap.put("Units Lost",  new ArrayList<Integer>(base));
		setmap.put("Combat Units",  new ArrayList<Integer>(base));
		setmap.put("Buildings",  new ArrayList<Integer>(base));
		values.put(teamid, setmap);
		teams.add(teamid);
	}
	
	/**
	 * a test if the mappings have been set up correctly
	 * 
	 * @return true iff the map has been set correctly 
	 */
	public boolean isSet() {
		return values.size() != 0;
	}
	
	/**
	 * Updates the information about a teams units when an enity is added
	 * 
	 * @param enity BaseEntity the entity to be updated added to the world
	 */
	public void updateunit(BaseEntity enity) {
		int teamid = enity.getOwner();
		if(!values.containsKey(teamid)) {
			return;
		}
		int count = values.get(teamid).get("Units").get(index);
		LOGGER.info("unit count " + count + " teamid " + teamid);
		count++;
		values.get(teamid).get("Units").set(index, count);
		if(enity instanceof AttackableEntity) {
			count = values.get(teamid).get("Combat Units").get(index);
			count++;
			values.get(teamid).get("Combat Units").set(index, count);
		}
		else if(enity instanceof BuildingEntity) {
			count = values.get(teamid).get("Buildings").get(index);
			count++;
			values.get(teamid).get("Buildings").set(index, count);
		}
	}
	
	/**
	 * Updates the information about a teams units when an enity is killed
	 * 
	 * @param enity BaseEntity the entity that has been killed
	 */
	public void updateDead(BaseEntity enity) {
		int teamid = enity.getOwner();
		if(!values.containsKey(teamid)) {
			return;
		}
		int count = values.get(teamid).get("Units").get(index);
		int dead = values.get(teamid).get("Units Lost").get(index);
		count--;
		dead++;
		values.get(teamid).get("Units").set(index, count);
		values.get(teamid).get("Units Lost").set(index, dead);
		if(enity instanceof AttackableEntity) {
			count = values.get(teamid).get("Combat Units").get(index);
			count--;
			values.get(teamid).get("Combat Units").set(index, count);
		}
		else if(enity instanceof BuildingEntity) {
			count = values.get(teamid).get("Buildings").get(index);
			count--;
			values.get(teamid).get("Buildings").set(index, count);
		}
	}
	
	/**
	 * checks how many teams are alive, also sets alive to a teamid that is alive
	 * 
	 * @return count int the number of teams still alive
	 */
	public int teamsAlive() {
		int count = 0;
		for(int t: values.keySet()) {
			if(values.get(t).get("Units").get(index) != 0) {
				alive = t;
				count++;
				
			}
		}
		return count;
	}
	
	/**
	 * Returns a team that is alive, no rules for which one
	 * 
	 * @return the teamid of a team that is alive
	 * @ensure teamsAlive is called before this
	 */
	public int getAlive() {
		return alive;
	}
	
	/**
	 * returns the histoy of of a set of points i.e resources
	 * 
	 * @ensure history is a valid field i.e Biomass, Units
	 * @param teamid int the teamid for the history
	 * @param history String the type of history to return i.e biomass
	 * @return float[] an array of the history of this field 
	 */
	public float[] getHistory(int teamid, String history){
		float[] returnv = new float[(index+2)*2];
		returnv[0] = 0;
		returnv[1] = 0;
		for(int i = 0; i < index; i+=2) {
			returnv[i + 2] = this.values.get(teamid).get(history).get(i-i);
			returnv[i + 3] = this.values.get(teamid).get(history).get(i-i);
		}
		return returnv;
	}
	
	/**
	 * gives a count of a current field
	 * 
	 * @ensure field is a valid field i.e Biomass, Units
	 * @param teamid int the teamid of the team being counted
	 * @param field string the field being counted
	 * @return int the count of this field
	 */
	public int count(int teamid, String field) {
		return values.get(teamid).get(field).get(index);
	}
	

}
